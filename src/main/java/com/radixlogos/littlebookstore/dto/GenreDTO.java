package com.radixlogos.littlebookstore.dto;

import com.radixlogos.littlebookstore.entities.Genre;

import java.util.List;

public record GenreDTO(Long id, String name, List<BookDTO> books) {

    public static GenreDTO fromGenre(Genre genre){
        return new GenreDTO(genre.getId(),genre.getName(),genre.getBooks().stream().map(BookDTO::fromBook).toList());
    }
}
