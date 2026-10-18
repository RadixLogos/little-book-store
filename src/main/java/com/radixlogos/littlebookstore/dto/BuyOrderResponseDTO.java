package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.Order;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record BuyOrderResponseDTO(
        Long id,
        @NotNull(message = "Must inform the client")
        ClientDTO client,
        @NotNull(message = "Must inform the date that the book was sold")
        LocalDate orderDate,
        @NotNull(message = "Must inform the books that were ordered")
        List<OrderRequestDTO> orders,
        String receiptUrl,
        Double total) {

        public static BuyOrderResponseDTO fromBuyOrder(BuyOrder buyOrder){
        var client = ClientDTO.fromClient(buyOrder.getClient());
        List<OrderRequestDTO> orderBooks = new ArrayList<>();
        for(Order ob : buyOrder.getOrders() ){
                orderBooks.add(OrderRequestDTO.fromOrder(ob));
        }
        return new BuyOrderResponseDTO(buyOrder.getId(),client,buyOrder.getOrderDate(),orderBooks, buyOrder.getReceiptUrl(), buyOrder.getTotal());
    }
}
