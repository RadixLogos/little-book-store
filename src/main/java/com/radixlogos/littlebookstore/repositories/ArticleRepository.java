package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.Article;
import com.radixlogos.littlebookstore.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    @Query(value = "SELECT * FROM tb_articles ORDER BY stock_quantity desc , name asc" , nativeQuery = true)
    Page<Article> findAllPaged(Pageable pageable);

    @Query("SELECT obj FROM Article obj WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%',:articleName,'%')) ORDER BY obj.name asc, obj.stockQuantity desc")
    Page<Article> findAllPagedByName(Pageable pageable, String articleName);

}
