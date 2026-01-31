package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.BookDTO;
import com.radixlogos.littlebookstore.dto.OrderBookDTO;
import com.radixlogos.littlebookstore.services.BookService;
import com.radixlogos.littlebookstore.services.OrderBookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService service;

    @GetMapping
    public ResponseEntity<Page<BookDTO>> findAll(
            Pageable pageable,
            @RequestParam(defaultValue = "") String name){
        var response = service.findAllBooks(pageable,name);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id){
        var response = service.findBookById(id);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping
    public ResponseEntity<BookDTO> insertBook(@Valid @RequestBody BookDTO bookDTO){
        var response = service.insertBook(bookDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<BookDTO> updateBook(
                @PathVariable Long id,
                @Valid @RequestBody BookDTO bookDTO){
        var response = service.updateBook(id,bookDTO);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id){
        service.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}

