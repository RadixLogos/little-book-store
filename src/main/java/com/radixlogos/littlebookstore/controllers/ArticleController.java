package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.ArticleDTO;
import com.radixlogos.littlebookstore.services.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleService service;

    @GetMapping
    public ResponseEntity<Page<ArticleDTO>> findAll(
            Pageable pageable,
            @ModelAttribute ArticleDTO articleDTO){
        var response = service.findAllArticles(pageable,articleDTO);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable Long id){
        var response = service.findArticleById(id);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ArticleDTO> insertArticle(@Valid @RequestBody ArticleDTO articleDTO){
        var response = service.insertArticle(articleDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasAnyRole('ADMIN')")
        public ResponseEntity<ArticleDTO> updateArticle(
                @PathVariable Long id,
                @Valid @RequestBody ArticleDTO articleDTO){
        var response = service.updateArticle(id,articleDTO);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        service.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }
}

