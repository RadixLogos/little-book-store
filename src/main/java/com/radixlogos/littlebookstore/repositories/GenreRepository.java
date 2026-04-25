package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.Genre;
import com.radixlogos.littlebookstore.entities.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GenreRepository extends JpaRepository<Genre,Long> {
}
