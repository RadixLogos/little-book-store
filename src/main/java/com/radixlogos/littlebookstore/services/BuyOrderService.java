package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.BookDTO;
import com.radixlogos.littlebookstore.dto.BuyOrderRequestDTO;
import com.radixlogos.littlebookstore.dto.BuyOrderResponseDTO;
import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.entities.Book;
import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.OrderBook;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import com.radixlogos.littlebookstore.repositories.BuyOrderRepository;
import com.radixlogos.littlebookstore.repositories.ClientRepository;
import com.radixlogos.littlebookstore.repositories.OrderBookRepository;
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
    @Autowired
    private OrderBookRepository orderBookRepository;
    private boolean isUpdate = false;
    @Transactional(readOnly = true)
    public Page<BuyOrderResponseDTO> findAllBuyOrders(Pageable pageable, String clientName, String bookName){

        return buyOrderRepository.findAllPaged(pageable, clientName, bookName).map(BuyOrderResponseDTO::fromBuyOrder);
    }
    @Transactional(readOnly = true)
    public BuyOrderResponseDTO findOrderById(Long buyOrderId){
        return BuyOrderResponseDTO
                .fromBuyOrder(buyOrderRepository.findById(buyOrderId)
                        .orElseThrow(()-> new ResourceNotFoundException("Pedido não encontrado")));
    }
    @Transactional
    public BuyOrderRequestDTO insertBuyOrder(BuyOrderRequestDTO  buyOrderDTO){
        var buyOrderEntity = new BuyOrder();
        copyDtoToEntity(buyOrderDTO,buyOrderEntity);
        createOrderBook(buyOrderDTO,buyOrderEntity);
        buyOrderEntity = buyOrderRepository.save(buyOrderEntity);
        return BuyOrderRequestDTO.entityToDTO(buyOrderEntity);
    }
    @Transactional
    public BuyOrderRequestDTO updateBuyOrder(Long id, BuyOrderRequestDTO buyOrderDTO){
        isUpdate = true;
        if (!buyOrderRepository.existsById(id)){
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        var buyOrderEntity = buyOrderRepository.getReferenceById(id);

        copyDtoToEntity(buyOrderDTO,buyOrderEntity);
        buyOrderEntity = buyOrderRepository.save(buyOrderEntity);
        return BuyOrderRequestDTO.entityToDTO(buyOrderEntity);
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
    private void copyDtoToEntity(BuyOrderRequestDTO buyOrderDTO, BuyOrder buyOrderEntity) {
        var client = clientRepository.findById(buyOrderDTO.clientId())
                .orElseThrow(()-> new ResourceNotFoundException("Cliente não encontrado"));
        client.setName(buyOrderDTO.clientName() == null ? client.getName() : buyOrderDTO.clientName());
        buyOrderEntity.setClient(client);
        buyOrderEntity.setOrderDate(buyOrderDTO.orderDate());
        buyOrderEntity.setReceiptUrl(buyOrderDTO.receiptUrl());

    }
    private void createOrderBook(BuyOrderRequestDTO buyOrderDTO, BuyOrder buyOrderEntity){
        Double total = 0.0;

        for(OrderBookDTO orderBookDTO : buyOrderDTO.orderBooks()) {
            buyOrderEntity.setTotal(total);
            var book = findBook(orderBookDTO.bookId());
            if (verifyUpdate(orderBookDTO)) {
                manageStock(book, orderBookDTO.quantity());
            }
            var orderBook = new OrderBook();
            orderBook.setBook(book);
            orderBook.setQuantity(orderBookDTO.quantity());
            orderBook.setPixValue(orderBookDTO.pixValue());
            orderBook.setMoneyValue(orderBookDTO.moneyValue());
            orderBook.setSoldValue(orderBook.getMoneyValue() + orderBook.getPixValue());
            orderBook.setSubTotal(calculateOrderBookSubtotal(orderBookDTO.soldValue(), orderBookDTO.quantity()));
            orderBook.setBuyOrder(buyOrderEntity);
            total += orderBook.getSubTotal();
            buyOrderEntity.addOrderBooks(orderBook);
        }
        buyOrderEntity.setTotal(total);
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
private boolean verifyUpdate(OrderBookDTO orderBookDTO){
        if(orderBookDTO.id() == null){
            return true;
        }
        var orderBook = orderBookRepository.findById(orderBookDTO.id()).orElseThrow(
                ()-> new ResourceNotFoundException("Livro do pedido não encontrado"));
    return orderBook.getQuantity() != orderBookDTO.quantity();
}
}
