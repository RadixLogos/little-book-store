package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record BuyOrderRequestDTO(
        Long id,
        @NotNull(message = "Must inform the client")
        Long clientId,
        String clientName,
        @NotNull(message = "Must inform the date that the book was sold")
        LocalDate orderDate,
        String receiptUrl,
        @NotNull(message = "Must inform the books that were ordered")
        List<OrderBookDTO> orderBooks
        ) {

    public static BuyOrderRequestDTO entityToDTO(BuyOrder buyOrder){
        return new BuyOrderRequestDTO(
                buyOrder.getId(),
                buyOrder.getClient().getId(),
                buyOrder.getClient().getName(),
                buyOrder.getOrderDate(),
                buyOrder.getReceiptUrl(),
                buyOrder.getOrderBooks().stream().map( OrderBookDTO::fromOrderBook).toList());
    }
}
