package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.entities.OrderBook;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import com.radixlogos.littlebookstore.repositories.OrderBookRepository;
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
    /**The method returns OrderBookDTO with subtotal**/
    @Transactional(readOnly = true)
    public Page<OrderBookDTO> findAllOrderBooks(Pageable pageable){
        Page<OrderBook> response = orderBookRepository.findAll(pageable);
        return response.map(OrderBookDTO::fromOrderBook);
    }
    @Transactional
    public OrderBookDTO findOrderById(Long orderId){
        var response = orderBookRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado"));
        return OrderBookDTO.fromOrderBook(response);
    }

    private Double getSubtotal(OrderBook orderBook){
        return orderBook.getSoldValue()* orderBook.getQuantity();
    }
}
