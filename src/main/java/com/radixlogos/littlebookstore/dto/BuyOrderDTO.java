package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.enums.PaymentType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record BuyOrderDTO(
        Long id,
        @NotNull(message = "Must inform the payment type")
        PaymentType paymentType,
        @NotNull(message = "Must inform the client")
        ClientDTO client,
        @NotNull(message = "Must inform the date that the book was sold")
        LocalDate orderDate,
        @NotNull(message = "Must inform the books that were ordered")
        List<OrderBookDTO> orderBooks,
        Double total) {
    public static BuyOrderDTO fromBuyOrder(BuyOrder buyOrder){
        var client = ClientDTO.fromClient(buyOrder.getClient());
        List<OrderBookDTO> orderBooks = new ArrayList<>();
        buyOrder.getOrderBooks().forEach(ob ->{
            orderBooks.add(OrderBookDTO.fromOrderBook(ob));
        });
        return new BuyOrderDTO(buyOrder.getId(),buyOrder.getPaymentType(),client,buyOrder.getOrderDate(),orderBooks, buyOrder.getTotal());
    }
}
