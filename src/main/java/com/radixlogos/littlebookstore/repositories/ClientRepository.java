package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.BuyOrder;
import com.radixlogos.littlebookstore.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    @Query("SELECT obj FROM Client obj WHERE UPPER(obj.name) LIKE UPPER(CONCAT(:name,'%')) AND obj.cellphone LIKE CONCAT(:cellphone,'%')")
    public Page<Client> findAllClients(Pageable pageable, String name, String cellphone);
}
