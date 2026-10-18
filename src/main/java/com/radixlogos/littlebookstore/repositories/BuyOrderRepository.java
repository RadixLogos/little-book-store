package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyOrderRepository extends JpaRepository<BuyOrder,Long> {
    @Query(value = "SELECT obj FROM BuyOrder obj JOIN FETCH obj.orders ob JOIN obj.client c WHERE UPPER(c.name) " +
            "LIKE UPPER(CONCAT(:clientName,'%'))",
            countQuery = "SELECT COUNT (DISTINCT obj) FROM BuyOrder obj JOIN obj.orders")
    public Page<BuyOrder> findAllPaged(Pageable pageable,String clientName, String bookNAme);
}
