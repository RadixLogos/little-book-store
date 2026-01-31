package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.entities.Book;
import com.radixlogos.littlebookstore.entities.OrderBook;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import com.radixlogos.littlebookstore.repositories.OrderBookRepository;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
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
    public OrderBookDTO findOrderBookById(Long orderId){
        var response = orderBookRepository.findById(orderId)
                .orElseThrow(()-> new ResourceNotFoundException("Pedido não encontrado"));
        return OrderBookDTO.fromOrderBook(response);
    }
    @Transactional
    public OrderBookDTO insertOrderBook(OrderBookDTO orderBookDTO){
        var orderBookEntity = new OrderBook();
        var book = bookRepository.findById(orderBookDTO.book().id())
                .orElseThrow(()-> new ResourceNotFoundException("Livro não encontrado"));
        copyDtoToEntity(orderBookDTO,orderBookEntity,book);
        orderBookEntity = orderBookRepository.save(orderBookEntity);
       return OrderBookDTO.fromOrderBook(orderBookEntity);
    }
    @Transactional
    public OrderBookDTO updateOrderBook(Long orderBookId,OrderBookDTO orderBookDTO){
        var orderBookEntity = orderBookRepository.getReferenceById(orderBookId);
        if(orderBookEntity.getId() == null)
            throw new ResourceNotFoundException("Pedido não encontrado");
        if(!bookRepository.existsById(orderBookDTO.book().id())){
            throw new ResourceNotFoundException("Livro não encontrado");
        }
        var book = bookRepository.getReferenceById(orderBookDTO.book().id());
        copyDtoToEntity(orderBookDTO,orderBookEntity,book);
        orderBookEntity = orderBookRepository.save(orderBookEntity);
        return OrderBookDTO.fromOrderBook(orderBookEntity);
    }
    @Transactional
    public void deleteOrderBook(Long id){
        if(!orderBookRepository.existsById(id)){
            throw new ResourceNotFoundException("Pedido de livro não encontrado");
        }
        try{
            orderBookRepository.deleteById(id);
        } catch (Exception e) {
            throw new DatabaseException("Falaha de integridade referencial");
        }
    }

    private void copyDtoToEntity(OrderBookDTO orderBookDTO, OrderBook orderBookEntity, Book book) {
        orderBookEntity.setBook(book);
        orderBookEntity.setSoldValue(orderBookDTO.soldValue());
        orderBookEntity.setQuantity(orderBookDTO.quantity());
        orderBookEntity.setSubTotal(getSubtotal(orderBookEntity));

        //Update the book
        book.setStockQuantity(book.getStockQuantity() - orderBookDTO.quantity());
        book.addOrderBook(orderBookEntity);
        bookRepository.save(book);
    }

    /**The method returns OrderBook subtotal**/
    private Double getSubtotal(OrderBook orderBook){
        return orderBook.getSoldValue()* orderBook.getQuantity();
    }
}
