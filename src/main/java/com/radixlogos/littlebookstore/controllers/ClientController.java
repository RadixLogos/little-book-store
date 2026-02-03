package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.dto.ClientDTO;
import com.radixlogos.littlebookstore.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clients")
public class ClientController {
    @Autowired
    private ClientService service;

    @GetMapping
    public ResponseEntity<Page<ClientDTO>> findAll(
            Pageable pageable,
            @RequestParam(defaultValue = "") String name){
        var response = service.findAllClients(pageable,name);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id){
        var response = service.findClientById(id);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping
    public ResponseEntity<ClientDTO> insertClient(@Valid @RequestBody ClientDTO clientDTO){
        var response = service.insertClient(clientDTO);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri).body(response);
        }

        @PutMapping("/{id}")
        public ResponseEntity<ClientDTO> updateClient(
                @PathVariable Long id,
                @Valid @RequestBody ClientDTO clientDTO){
        var response = service.updateClient(id,clientDTO);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        service.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}

