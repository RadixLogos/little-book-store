package com.radixlogos.littlebookstore.controllers;

import com.radixlogos.littlebookstore.services.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Map<String,Object>> upload(
            @RequestParam("file") MultipartFile file           ) {

        return  ResponseEntity.ok().body(imageService.uploadImage(file));
    }
}
