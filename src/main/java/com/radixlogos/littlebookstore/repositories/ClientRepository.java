package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
}
