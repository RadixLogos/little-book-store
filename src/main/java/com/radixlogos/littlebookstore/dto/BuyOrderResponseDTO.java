package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.OrderBook;
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
        List<OrderBookDTO> orderBooks,
        Double total) {

        public static BuyOrderResponseDTO fromBuyOrder(BuyOrder buyOrder){
        var client = ClientDTO.fromClient(buyOrder.getClient());
        List<OrderBookDTO> orderBooks = new ArrayList<>();
        for(OrderBook ob : buyOrder.getOrderBooks() ){
                orderBooks.add(OrderBookDTO.fromOrderBook(ob));
        }
        return new BuyOrderResponseDTO(buyOrder.getId(),client,buyOrder.getOrderDate(),orderBooks, buyOrder.getTotal());
    }
}
