package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.services.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
