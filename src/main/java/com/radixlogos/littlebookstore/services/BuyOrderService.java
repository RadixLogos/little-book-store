package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.BuyOrderRequestDTO;
import com.radixlogos.littlebookstore.dto.BuyOrderResponseDTO;
import com.radixlogos.littlebookstore.dto.OrderRequestDTO;
import com.radixlogos.littlebookstore.entities.*;
import com.radixlogos.littlebookstore.repositories.*;
import com.radixlogos.littlebookstore.services.exceptions.BookException;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
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
    private OrderRepository orderRepository;
    @Autowired
    private ArticleRepository articleRepository;

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
    public BuyOrderRequestDTO insertBuyOrderBook(BuyOrderRequestDTO  buyOrderDTO){
        var buyOrderEntity = new BuyOrder();
        copyDtoToEntity(buyOrderDTO,buyOrderEntity);
        createOrder(buyOrderDTO,buyOrderEntity);
        buyOrderEntity = buyOrderRepository.save(buyOrderEntity);
        return BuyOrderRequestDTO.entityToDTO(buyOrderEntity);
    }

    @Transactional
    public BuyOrderRequestDTO updateBuyOrder(Long id, BuyOrderRequestDTO buyOrderDTO){
        if (!buyOrderRepository.existsById(id)){
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        var buyOrderEntity = buyOrderRepository.getReferenceById(id);

        copyDtoToEntity(buyOrderDTO,buyOrderEntity);
        updateOrderBooks(buyOrderDTO,buyOrderEntity);

        buyOrderEntity = buyOrderRepository.save(buyOrderEntity);
        return BuyOrderRequestDTO.entityToDTO(buyOrderEntity);
    }
    @Transactional
    public void deleteBuyOrder(Long id){
        if(!buyOrderRepository.existsById(id)){
            throw new ResourceNotFoundException("Pedido não encontrado");
        }
        try {
            BuyOrder buyOrder = buyOrderRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Pedido não encontrado"));

            for(Order order : buyOrder.getOrders()){
                if(order.getBook() != null){
                    updateBookQuantity(order.getBook().getId(),order.getQuantity());
                }
                if(order.getArticle() != null){
                    updateArticleQuantity(order.getArticle().getId(),order.getQuantity());
                }
            }
            buyOrderRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new DatabaseException("Falha de integridade referencial");
        }
    }

    private void updateBookQuantity(Long bookId, Integer restoreQuantity) {
        Book book = bookRepository.getReferenceById(bookId);
        book.setStockQuantity(book.getStockQuantity() + restoreQuantity);
        bookRepository.save(book);
    }

    private void updateArticleQuantity(Long articleId, Integer restoreQuantity) {
        Article article = articleRepository.getReferenceById(articleId);
        article.setStockQuantity(article.getStockQuantity() + restoreQuantity);
        articleRepository.save(article);
    }

    private void copyDtoToEntity(BuyOrderRequestDTO buyOrderDTO, BuyOrder buyOrderEntity) {
        var client = clientRepository.findById(buyOrderDTO.clientId())
                .orElseThrow(()-> new ResourceNotFoundException("Cliente não encontrado"));
        buyOrderEntity.setClient(client);
        buyOrderEntity.setOrderDate(buyOrderDTO.orderDate());
        buyOrderEntity.setReceiptUrl(buyOrderDTO.receiptUrl());

    }
    private void createOrder(BuyOrderRequestDTO buyOrderDTO, BuyOrder buyOrderEntity){
        Double total = 0.0;

        for(OrderRequestDTO orderDTO : buyOrderDTO.orders()) {
            buyOrderEntity.setTotal(total);
            if(orderDTO.itemType().equals("book")){
                var book = findBook(orderDTO.itemId());
                if(manageStock(book.getStockQuantity(), orderDTO.quantity())){
                    book.setStockQuantity(book.getStockQuantity() - orderDTO.quantity());
                };
                var orderBook = getOrder(buyOrderEntity, orderDTO);
                orderBook.setBook(book);
                total += orderBook.getSubTotal();
                buyOrderEntity.addOrders(orderBook);
            }
            if(orderDTO.itemType().equals("article")){
                var article = findArticle(orderDTO.itemId());
                if(manageStock(article.getStockQuantity(), orderDTO.quantity())){
                    article.setStockQuantity(article.getStockQuantity() - orderDTO.quantity());
                };
                var orderArticle = getOrder(buyOrderEntity, orderDTO);
                orderArticle.setArticle(article);
                total += orderArticle.getSubTotal();
                buyOrderEntity.addOrders(orderArticle);
            }

        }
        buyOrderEntity.setTotal(total);
    }


    private Order getOrder(BuyOrder buyOrderEntity, OrderRequestDTO orderDTO) {
        var order = new Order();
        order.setQuantity(orderDTO.quantity());
        order.setPixValue(orderDTO.pixValue());
        order.setMoneyValue(orderDTO.moneyValue());
        order.setSoldValue(order.getMoneyValue() + order.getPixValue());
        order.setSubTotal(calculateOrderBookSubtotal(orderDTO.pixValue() + orderDTO.moneyValue(), orderDTO.quantity()));
        order.setBuyOrder(buyOrderEntity);
        return order;
    }

    private void updateOrderBooks(BuyOrderRequestDTO buyOrderDTO, BuyOrder buyOrderEntity){
        if(buyOrderEntity.getOrders().size() > buyOrderDTO.orders().size()){
            createOrder(buyOrderDTO, buyOrderEntity);
        }
        Double total = 0.0;
        for(OrderRequestDTO orderBookDTO : buyOrderDTO.orders()){

            Order order = orderRepository.getReferenceById(orderBookDTO.id());
            if(orderBookDTO.itemType().equals("book")){
                Book book = bookRepository.getReferenceById(orderBookDTO.itemId());
                boolean manageStockUpdate = verifyUpdate(orderBookDTO,order);
                order.setBook(book);
                order.setMoneyValue(orderBookDTO.moneyValue());
                order.setPixValue(orderBookDTO.pixValue());
                order.setBuyOrder(buyOrderEntity);
                order.setQuantity(orderBookDTO.quantity());
                order.setSoldValue(orderBookDTO.soldValue());
                order.setSubTotal(order.getQuantity()* order.getSoldValue());
                if(manageStockUpdate){
                    if(manageStock(book.getStockQuantity(),order.getQuantity())){
                        book.setStockQuantity(book.getStockQuantity() - orderBookDTO.quantity());
                    };
                }
                total+= order.getSubTotal();
            }
            if(orderBookDTO.itemType().equals("article")){
                boolean manageStockUpdate = verifyUpdate(orderBookDTO,order);
                Article article = articleRepository.getReferenceById(orderBookDTO.itemId());
                order.setArticle(article);
                order.setMoneyValue(orderBookDTO.moneyValue());
                order.setPixValue(orderBookDTO.pixValue());
                order.setBuyOrder(buyOrderEntity);
                order.setQuantity(orderBookDTO.quantity());
                order.setSoldValue(orderBookDTO.soldValue());
                order.setSubTotal(order.getQuantity()* order.getSoldValue());
                if(manageStockUpdate){
                    if(manageStock(article.getStockQuantity(),order.getQuantity())){
                        article.setStockQuantity(article.getStockQuantity() - orderBookDTO.quantity());
                    };
                }
                total+= order.getSubTotal();
            }
            buyOrderEntity.setTotal(total);
            orderRepository.save(order);
        }

    }

    private Book findBook(Long id){
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
    }

    private Article findArticle(Long id){
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));
    }

    /**The method returns OrderBook subtotal**/
    private Double calculateOrderBookSubtotal(Double soldValue, Integer quantity){
        return soldValue*quantity;
    }

    private boolean manageStock(int stockQuantity, int requestedQuantity){

        if(stockQuantity < requestedQuantity){
            String error = "A quantidade no estoque é insuficiente";
            if(stockQuantity > 0){
                error += ". Quantidade em estoque: " + stockQuantity;
            }
            throw new BookException(error);
        } else
            return true;
    }
    private boolean verifyUpdate(OrderRequestDTO orderBookDTO, Order orderBookEntity){
        return orderBookEntity.getQuantity() != orderBookDTO.quantity();
    }
}
