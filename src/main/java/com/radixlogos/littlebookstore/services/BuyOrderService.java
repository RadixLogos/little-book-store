package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.BuyOrderDTO;
import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.entities.Book;
import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.OrderBook;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import com.radixlogos.littlebookstore.repositories.BuyOrderRepository;
import com.radixlogos.littlebookstore.repositories.ClientRepository;
import com.radixlogos.littlebookstore.services.exceptions.BookException;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuyOrderService {
    @Autowired
    private BuyOrderRepository buyOrderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<BuyOrderDTO> findAllBuyOrders(Pageable pageable){
        return buyOrderRepository.findAllPaged(pageable).map(BuyOrderDTO::fromBuyOrder);
    }
    @Transactional(readOnly = true)
    public BuyOrderDTO findOrderById(Long buyOrderId){
        return BuyOrderDTO
                .fromBuyOrder(buyOrderRepository.findById(buyOrderId)
                        .orElseThrow(()-> new ResourceNotFoundException("Pedido não encontrado")));
    }
    @Transactional
    public BuyOrderDTO insertBuyOrder(BuyOrderDTO buyOrderDTO){
        var buyOrderEntity = new BuyOrder();
        copyDtoToEntity(buyOrderDTO,buyOrderEntity);
        buyOrderEntity = buyOrderRepository.save(buyOrderEntity);
        return BuyOrderDTO.fromBuyOrder(buyOrderEntity);
    }
    @Transactional
    public BuyOrderDTO updateBuyOrder(Long id, BuyOrderDTO buyOrderDTO){
        if (!buyOrderRepository.existsById(id)){
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        var buyOrderEntity = buyOrderRepository.getReferenceById(id);
        copyDtoToEntity(buyOrderDTO,buyOrderEntity);
        buyOrderEntity = buyOrderRepository.save(buyOrderEntity);
        return BuyOrderDTO.fromBuyOrder(buyOrderEntity);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteBuyOrder(Long id){
        if(!buyOrderRepository.existsById(id)){
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        try {
            buyOrderRepository.deleteById(id);
        }catch (Exception e){
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
    private void copyDtoToEntity(BuyOrderDTO buyOrderDTO, BuyOrder buyOrderEntity) {
        var client = clientRepository.findById(buyOrderDTO.client().id())
                .orElseThrow(()-> new ResourceNotFoundException("Cliente não encontrado"));
        buyOrderEntity.setClient(client);
        buyOrderEntity.setOrderDate(buyOrderDTO.orderDate());
        buyOrderEntity.setPaymentType(buyOrderDTO.paymentType());

        Double total = 0.0;
        for(OrderBookDTO ob : buyOrderDTO.orderBooks()){
            var orderBookEntity = createOrderBook(ob,buyOrderEntity);
            total += orderBookEntity.getSubTotal();
            buyOrderEntity.addOrderBooks(orderBookEntity);
        };
        buyOrderEntity.setTotal(total);

    }
    private OrderBook createOrderBook(OrderBookDTO orderBookDTO, BuyOrder buyOrderEntity){
        var book = findBook(orderBookDTO.book().id());
        manageStock(book,orderBookDTO.quantity());
        var orderBook = new OrderBook();
        orderBook.setBook(book);
        orderBook.setQuantity(orderBookDTO.quantity());
        orderBook.setSoldValue(orderBookDTO.soldValue());
        orderBook.setSubTotal(calculateOrderBookSubtotal(orderBookDTO.soldValue(),orderBookDTO.quantity()));
        orderBook.setBuyOrder(buyOrderEntity);
        return orderBook;
    }

    private Book findBook(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
    }
    /**The method returns OrderBook subtotal**/
    private Double calculateOrderBookSubtotal(Double soldValue, Integer quantity){
        return soldValue*quantity;
    }
    private void manageStock(Book book, int requestedQuantity){
        if(book.getStockQuantity() < requestedQuantity){
            String error = "A quantidade do livro " + book.getName() +" no estoque é insuficiente";
            if(book.getStockQuantity() > 0){
                error += ". Quantidade em estoque: " + book.getStockQuantity();
            }
            throw new BookException(error);
        }
        book.setStockQuantity(book.getStockQuantity() - requestedQuantity);
    }
}
