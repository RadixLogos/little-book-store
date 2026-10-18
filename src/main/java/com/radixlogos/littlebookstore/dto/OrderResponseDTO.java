package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Order;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderResponseDTO(
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
    public static OrderResponseDTO fromOrderBook(Order orderBook){
        return new OrderResponseDTO(orderBook.getId(),orderBook.getQuantity(),orderBook.getSoldValue(), orderBook.getPixValue(), orderBook.getMoneyValue(), BookDTO.fromBook(orderBook.getBook()), orderBook.getSubTotal());
    }
}
