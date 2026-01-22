package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.entities.Book;
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
    public Page<OrderBookDTO> findAllOrders(Pageable pageable, String bookName){
        Page<OrderBook> response = orderBookRepository.findAllPaged(pageable,bookName);
        return response.map(o ->{
            var subTotal = getSubtotal(o.getSoldValue(), o.getQuantity());

            return OrderBookDTO.fromOrderBook(o,subTotal);
        });
    }
    @Transactional
    public OrderBookDTO findOrderById(Long orderId){
        var response = orderBookRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException("Pedido não encontrado"));
        var subTotal = getSubtotal(response.getSoldValue(), response.getQuantity());
        return OrderBookDTO.fromOrderBook(response, subTotal);
    }

    private Double getSubtotal(Double soldValue, Integer quantity){
        return soldValue*quantity;
    }
}
