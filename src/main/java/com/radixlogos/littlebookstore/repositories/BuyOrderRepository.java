package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.OrderBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyOrderRepository extends JpaRepository<BuyOrder,Long> {
    @Query(value = "SELECT obj FROM BuyOrder obj JOIN FETCH obj.orderBooks",
            countQuery = "SELECT COUNT (DISTINCT obj) FROM BuyOrder obj JOIN obj.orderBooks")
    public Page<BuyOrder> findAllPaged(Pageable pageable);
}
