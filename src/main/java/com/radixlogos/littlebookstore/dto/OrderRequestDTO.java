package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Order;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderRequestDTO(
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
        @NotNull(message = "It is necessary to inform the id of the item ordered")
        Long itemId,
        Double subtotal,
        String itemType) {
    public static OrderRequestDTO fromOrder(Order order){
        System.out.println(order.getSubTotal());
        String itemType = "";
        if(order.getArticle() != null && order.getArticle().getId() != null){
            itemType = "article";
        }
         else if(order.getBook() != null && order.getBook().getId() != null){
            itemType = "book";
        }
        return new OrderRequestDTO(
                order.getId(),
                (order.getBook() != null) ? order.getBook().getName() : order.getArticle().getName(),
                order.getQuantity(),
                order.getPixValue(),
                order.getMoneyValue(),
                order.getSoldValue(),
                (order.getBook() != null) ? order.getBook().getId() : order.getArticle().getId(),
                order.getSubTotal(),
                itemType);
    }
}
