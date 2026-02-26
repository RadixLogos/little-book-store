package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.OrderBook;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderBookDTO(
        Long id,
        @Positive(message = "Quantity can't be less then 1")
        Integer quantity,
        @PositiveOrZero(message = "Must be positive or zero")
        Double soldValue,
        @PositiveOrZero
        Double discount,
        @NotNull(message = "It is necessary to inform the book that was sold")
        BookDTO book,
        Double subtotal) {
    public static OrderBookDTO fromOrderBook(OrderBook orderBook){
        var bookDto = BookDTO.fromBook(orderBook.getBook());

        return new OrderBookDTO(orderBook.getId(),orderBook.getQuantity(), orderBook.getSoldValue(), orderBook.getDiscount(),bookDto, orderBook.getSubTotal());
    }
}
