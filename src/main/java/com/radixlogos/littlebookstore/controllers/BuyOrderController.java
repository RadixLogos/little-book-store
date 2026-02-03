package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.BuyOrderDTO;
import com.radixlogos.littlebookstore.services.BuyOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/buy-orders")
public class BuyOrderController {
    @Autowired
    private BuyOrderService service;

    @GetMapping
    public ResponseEntity<Page<BuyOrderDTO>> findAll(Pageable pageable){
        var response = service.findAllBuyOrders(pageable);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BuyOrderDTO> getBuyOrder(@PathVariable Long id){
        var response = service.findOrderById(id);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping
    public ResponseEntity<BuyOrderDTO> insertBuyOrder(@Valid @RequestBody BuyOrderDTO buyOrderDTO){
        var response = service.insertBuyOrder(buyOrderDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<BuyOrderDTO> updateBuyOrder(
                @PathVariable Long id,
                @Valid @RequestBody BuyOrderDTO buyOrderDTO){
        var response = service.updateBuyOrder(id, buyOrderDTO);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyOrder(@PathVariable Long id){
        service.deleteBuyOrder(id);
        return ResponseEntity.noContent().build();
    }
}

