package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.enums.PaymentType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record BuyOrderRequestDTO(
        Long id,
        @NotNull(message = "Must inform the payment type")
        PaymentType paymentType,
        @NotNull(message = "Must inform the client")
        Long client,
        @NotNull(message = "Must inform the date that the book was sold")
        LocalDate orderDate,
        @NotNull(message = "Must inform the books that were ordered")
        List<OrderBookDTO> orderBooks
        ) {

    public static BuyOrderRequestDTO entityToDTO(BuyOrder buyOrder){
        return new BuyOrderRequestDTO(
                buyOrder.getId(),
                buyOrder.getPaymentType(),
                buyOrder.getClient().getId(),
                buyOrder.getOrderDate(),
                buyOrder.getOrderBooks().stream().map( OrderBookDTO::fromOrderBook).toList());
    }
}
