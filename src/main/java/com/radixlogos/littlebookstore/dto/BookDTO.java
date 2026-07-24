package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Book;
import com.radixlogos.littlebookstore.entities.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record BookDTO(Long id,
                      @NotBlank(message = "Must inform the name")
                      String name,
                      @NotNull(message = "Must inform the editor")
                      EditorDTO editor,
                      String author,
                      String description,
                      List<Long> genreIds,
                      List<GenreDTO> genres,
                      @NotNull(message = "Must inform the price")
                      @PositiveOrZero(message = "The price must be a positive value")
                      Double price,
                      @NotNull(message = "Must inform the stock")
                      @PositiveOrZero(message = "The value must be positive or zero")
                      Integer stockQuantity,
                      String imgUrl
                      ) {

    public static BookDTO fromBook(Book book){
      List<Long> genreIds = book.getGenres().stream().map(Genre::getId).toList();
      EditorDTO editorDTO = new EditorDTO(book.getEditor().getId(), book.getEditor().getName());
      return new BookDTO(book.getId(), book.getName(), editorDTO, book.getAuthor(), book.getDescription(),genreIds,book.getGenres().stream().map(GenreDTO::fromGenre).toList(),book.getPrice(), book.getStockQuantity(), book.getImgUrl());
    }
}
