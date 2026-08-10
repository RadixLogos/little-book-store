package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.EditorDTO;
import com.radixlogos.littlebookstore.services.EditorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/editor")
public class EditorController {
    @Autowired
    private EditorService service;

    @GetMapping
    public ResponseEntity<Page<EditorDTO>> findAll(
            @RequestParam(defaultValue = "") String name, Pageable pageable){
        var response = service.findAllEditors(pageable);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EditorDTO> getEditor(@PathVariable Long id){
        var response = service.findEditorById(id);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping
    public ResponseEntity<EditorDTO> insertEditor(@Valid @RequestBody EditorDTO bookDTO){
        var response = service.insertEditor(bookDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<EditorDTO> updateEditor(
                @PathVariable Long id,
                @Valid @RequestBody EditorDTO bookDTO){
        var response = service.updateEditor(id,bookDTO);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEditor(@PathVariable Long id){
        service.deleteEditor(id);
        return ResponseEntity.noContent().build();
    }
}

