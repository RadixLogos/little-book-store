package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.GenreDTO;
import com.radixlogos.littlebookstore.entities.Genre;
import com.radixlogos.littlebookstore.repositories.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public Page<GenreDTO> getAllGenres(Pageable pageable){
        return genreRepository.findAll(pageable).map(GenreDTO::fromGenre);
    }

    @Transactional
    public GenreDTO insertGenre(GenreDTO genreDTO) {
        var genreEntity = new Genre();
        genreEntity.setName(genreDTO.name());
        return GenreDTO.fromGenre(genreRepository.save(genreEntity));
    }

    @Transactional(readOnly = true)
    public GenreDTO findGenreById(Long id){
        return GenreDTO.fromGenre(genreRepository
                .findById(id)
                .orElseThrow(
                        ()-> new EntityNotFoundException("Gênero não encontrado"))
        );
    }

    @Transactional
    public GenreDTO updateGenre(Long id, GenreDTO genreDTO) {
        var genreEntity = genreRepository.getReferenceById(id);
        genreEntity.setName(genreDTO.name());
        return GenreDTO.fromGenre(genreRepository.save(genreEntity));
    }
}
