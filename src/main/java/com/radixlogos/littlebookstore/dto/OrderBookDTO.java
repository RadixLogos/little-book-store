package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.OrderBook;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderBookDTO(
        Long id,
        Long buyOrderId,
        @Positive(message = "Quantity can't be less then 1")
        Integer quantity,
        @PositiveOrZero(message = "Must be positive or zero")
        Double soldValue,
        @NotNull(message = "It is necessary to inform the book that was sold")
        BookDTO book,
        Double subtotal) {
    public static OrderBookDTO fromOrderBook(OrderBook orderBook){
        var bookDto = BookDTO.fromBook(orderBook.getBook());
        Long buyOrderId = null;
        if(orderBook.getBuyOrder() != null ){
            buyOrderId = orderBook.getBuyOrder().getId();
        }
        return new OrderBookDTO(orderBook.getId(), buyOrderId, orderBook.getQuantity(), orderBook.getSoldValue(), bookDto, orderBook.getSubTotal());
    }
}
