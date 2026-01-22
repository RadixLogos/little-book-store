package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.OrderBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OrderBookRepository extends JpaRepository<OrderBook,Long> {
    @Query(value = "SELECT obj FROM OrderBook JOIN FETCH obj.book WHERE UPPER(obj.book.name) LIKE UPPER(CONCAT(:name,'%'))",
            countQuery = "SELECT COUNT(DISTINCT obj) FROM OrderBook obj JOIN obj.book")
    Page<OrderBook> findAllPaged(Pageable pageable, String bookName);
}
