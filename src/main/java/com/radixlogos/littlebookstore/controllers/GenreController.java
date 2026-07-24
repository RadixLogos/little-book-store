package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.GenreDTO;
import com.radixlogos.littlebookstore.entities.Genre;
import com.radixlogos.littlebookstore.services.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<GenreDTO>> getAllGenres(){
        List<GenreDTO> genreDTOS = genreService.getAllGenres();
        return ResponseEntity.ok(genreDTOS);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> findById(@RequestParam Long id){
        return ResponseEntity.ok(genreService.findGenreById(id));
    }
    @PostMapping
    public ResponseEntity<GenreDTO> insertGenre(@RequestBody GenreDTO genreDTO){
        GenreDTO response = genreService.insertGenre(genreDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenreDTO> updateGenre(@RequestParam Long id, @RequestBody GenreDTO genreDTO){
        return ResponseEntity.ok(genreService.updateGenre(id,genreDTO));
    }

}
