package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order,Long> {
}
