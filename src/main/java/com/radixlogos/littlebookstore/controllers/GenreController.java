package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.GenreDTO;
import com.radixlogos.littlebookstore.entities.Genre;
import com.radixlogos.littlebookstore.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<Page<GenreDTO>> getAllGenres(Pageable pageable){
        Page<GenreDTO> genreDTOS = genreService.getAllGenres(pageable);
        return ResponseEntity.ok(genreDTOS);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<GenreDTO> findById(@RequestParam Long id){
        return ResponseEntity.ok(genreService.findGenreById(id));
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<GenreDTO> insertGenre(@RequestBody GenreDTO genreDTO){
        GenreDTO response = genreService.insertGenre(genreDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','OPERATOR')")
    public ResponseEntity<GenreDTO> updateGenre(@RequestParam Long id, @RequestBody GenreDTO genreDTO){
        return ResponseEntity.ok(genreService.updateGenre(id,genreDTO));
    }

}
