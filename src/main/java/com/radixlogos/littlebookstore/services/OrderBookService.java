package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.entities.OrderBook;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import com.radixlogos.littlebookstore.repositories.OrderBookRepository;
import com.radixlogos.littlebookstore.services.exceptions.BookNotFoundException;
import com.radixlogos.littlebookstore.services.exceptions.OrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderBookService {
    @Autowired
    private OrderBookRepository orderBookRepository;
    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<OrderBookDTO> findAllOrderBooks(Pageable pageable){
        Page<OrderBook> response = orderBookRepository.findAll(pageable);
        return response.map(OrderBookDTO::fromOrderBook);
    }
    @Transactional(readOnly = true)
    public OrderBookDTO findOrderById(Long orderId){
        var response = orderBookRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado"));
        return OrderBookDTO.fromOrderBook(response);
    }
    @Transactional
    public OrderBookDTO insertOrderBook(OrderBookDTO orderBookDTO){
        var orderBookEntity = new OrderBook();
        copyDtoToEntity(orderBookDTO,orderBookEntity);
        orderBookEntity = orderBookRepository.save(orderBookEntity);
       return OrderBookDTO.fromOrderBook(orderBookEntity);
    }
    @Transactional
    public OrderBookDTO updateOrderBook(Long orderBookId,OrderBookDTO orderBookDTO){
        var orderBookEntity = orderBookRepository.getReferenceById(orderBookId);
        if(orderBookEntity.getId() == null)
            throw new OrderNotFoundException("Pedido não encontrado");
        copyDtoToEntity(orderBookDTO,orderBookEntity);
        orderBookEntity = orderBookRepository.save(orderBookEntity);
        return OrderBookDTO.fromOrderBook(orderBookEntity);
    }
    private void copyDtoToEntity(OrderBookDTO orderBookDTO, OrderBook orderBookEntity) {
        var book = bookRepository.findById(orderBookDTO.bookId())
                .orElseThrow(() ->new BookNotFoundException("Livro não encontrado"));
        orderBookEntity.setBook(book);
        orderBookEntity.setSoldValue(orderBookDTO.soldValue());
        orderBookEntity.setQuantity(orderBookDTO.quantity());
        orderBookEntity.setSubTotal(getSubtotal(orderBookEntity));
    }

    /**The method returns OrderBook subtotal**/
    private Double getSubtotal(OrderBook orderBook){
        return orderBook.getSoldValue()* orderBook.getQuantity();
    }
}
