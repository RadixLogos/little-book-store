package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.OrderBook;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record OrderBookDTO(
        Long id,
        String name,
        @Positive(message = "Quantity can't be less then 1")
        Integer quantity,
        @PositiveOrZero(message = "Must be positive or zero")
        Double pixValue,
        @PositiveOrZero(message = "Must be positive or zero")
        Double moneyValue,
        @PositiveOrZero(message = "Must be positive or zero")
        Double soldValue,
        @NotNull(message = "It is necessary to inform the book that was sold")
        Long bookId,
        Double subtotal) {
    public static OrderBookDTO fromOrderBook(OrderBook orderBook){
        System.out.println(orderBook.getSubTotal());
        return new OrderBookDTO(
                orderBook.getId(),
                orderBook.getBook().getName(),
                orderBook.getQuantity(),
                orderBook.getPixValue(),
                orderBook.getMoneyValue(),
                orderBook.getSoldValue(),
                orderBook.getBook().getId(),
                orderBook.getSubTotal());
    }
}
