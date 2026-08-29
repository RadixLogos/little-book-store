package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.BuyOrderRequestDTO;
import com.radixlogos.littlebookstore.dto.BuyOrderResponseDTO;
import com.radixlogos.littlebookstore.services.BuyOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/buy-orders")
public class BuyOrderController {
    @Autowired
    private BuyOrderService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<Page<BuyOrderResponseDTO>> findAll(
            Pageable pageable,
            @RequestParam(name = "clientName", defaultValue = "", required = false)  String clientName,
            @RequestParam(name = "bookName", defaultValue = "", required = false)  String bookName){
        var response = service.findAllBuyOrders(pageable, clientName, bookName);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<BuyOrderResponseDTO> getBuyOrder(@PathVariable Long id){
        var response = service.findOrderById(id);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<BuyOrderRequestDTO> insertBuyOrder(@Valid @RequestBody BuyOrderRequestDTO buyOrderDTO){

        var response = service.insertBuyOrder(buyOrderDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<BuyOrderRequestDTO> updateBuyOrder(
                @PathVariable Long id,
                @Valid @RequestBody BuyOrderRequestDTO buyOrderDTO){
        var response = service.updateBuyOrder(id, buyOrderDTO);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteBuyOrder(@PathVariable Long id){
        service.deleteBuyOrder(id);
        return ResponseEntity.noContent().build();
    }
}

