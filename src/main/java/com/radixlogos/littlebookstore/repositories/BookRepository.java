package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book,Long> {
    @Query("SELECT obj FROM Book obj WHERE UPPER(obj.name) LIKE UPPER(CONCAT(:name,'%'))")
    public Page<Book> findAllPaged(Pageable pageable, String name);
}
