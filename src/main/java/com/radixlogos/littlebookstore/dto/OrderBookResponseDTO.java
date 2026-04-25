package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.OrderBook;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderBookResponseDTO(
        Long id,
        @Positive(message = "Quantity can't be less then 1")
        Integer quantity,
        @PositiveOrZero(message = "Must be positive or zero")
        Double pixValue,
        @PositiveOrZero(message = "Must be positive or zero")
        Double moneyValue,
        @PositiveOrZero(message = "Must be positive or zero")
        Double soldValue,
        @NotNull(message = "It is necessary to inform the book that was sold")
        BookDTO book,
        Double subtotal) {
    public static OrderBookResponseDTO fromOrderBook(OrderBook orderBook){
        return new OrderBookResponseDTO(orderBook.getId(),orderBook.getQuantity(),orderBook.getSoldValue(), orderBook.getPixValue(), orderBook.getMoneyValue(), BookDTO.fromBook(orderBook.getBook()), orderBook.getSubTotal());
    }
}
