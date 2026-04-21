package com.radixlogos.littlebookstore.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.Map;

@Service
public class ImageService {

    @Autowired
    private BookRepository bookRepository;

    private final Cloudinary cloudinary;

    public ImageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public Map<String, Object> uploadImage(MultipartFile file) {
        try {
            var map = (Map<String, Object>) cloudinary.uploader().upload(file.getBytes(), Map.of());
            return  map;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload da imagem : "  + e.getMessage());
        }
    }

    private String slug(String text) {

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);

        return normalized
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "") // remove acentos
                .toLowerCase()
                .replace(" ", "-")
                .replaceAll("[^a-z0-9-]", "");

    }
}