package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.services.OrderBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/order-books")
public class OrderBookController {
    @Autowired
    private OrderBookService service;

    @GetMapping
    public ResponseEntity<Page<OrderBookDTO>> findAll(Pageable pageable){
        var response = service.findAllOrderBooks(pageable);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderBookDTO> getOrderBook(@PathVariable Long id){
        var response = service.findOrderById(id);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping
    public ResponseEntity<OrderBookDTO> insertOrderBook(@Valid @RequestBody OrderBookDTO orderBookDTO){
        var response = service.insertOrderBook(orderBookDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<OrderBookDTO> updateOrderBook(
                @PathVariable Long id,
                @Valid @RequestBody OrderBookDTO orderBookDTO){
        var response = service.updateOrderBook(id,orderBookDTO);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderBook(@PathVariable Long id){
        service.deleteOrderBook(id);
        return ResponseEntity.noContent().build();
    }
}

