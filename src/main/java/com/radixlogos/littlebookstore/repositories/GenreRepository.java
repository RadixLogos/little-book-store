package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.Genre;
import com.radixlogos.littlebookstore.entities.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface GenreRepository extends JpaRepository<Genre,Long> {
    @Modifying
    @Query(value = "delete from tb_genre_book where genre_id not in(:genreIds) and book_id = :bookId", nativeQuery = true)
    public void deleteWhenNotInUpdate(List<Long> genreIds, Long bookId);


}
