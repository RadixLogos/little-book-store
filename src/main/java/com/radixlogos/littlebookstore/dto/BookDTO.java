package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record BookDTO(Long id,
                      @NotBlank(message = "Must inform the name")
                      String name,
                      @NotBlank(message = "Must inform the editor")
                      String editor,
                      @NotBlank(message = "Must inform the price")
                      @PositiveOrZero(message = "The price must be a positive value")
                      Double price,
                      @NotBlank(message = "Must inform the stock")
                      @PositiveOrZero(message = "The value must be positive or zero")
                      Integer stockQuantity) {

    public static BookDTO fromBook(Book book){
        return new BookDTO(book.getId(), book.getName(), book.getEditor(), book.getPrice(), book.getStockQuantity());
    }
}
