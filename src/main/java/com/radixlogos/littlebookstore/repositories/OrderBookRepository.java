package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.OrderBook;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderBookRepository extends JpaRepository<OrderBook,Long> {
}
