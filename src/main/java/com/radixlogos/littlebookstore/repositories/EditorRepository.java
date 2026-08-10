package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.entities.Editor;
import com.radixlogos.littlebookstore.entities.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface EditorRepository extends JpaRepository<Editor,Long> {
    @Query(value = "SELECT * FROM tb_editor e ORDER BY e.name asc", nativeQuery = true)
    Page<Editor> findAllEditors(Pageable pageable);
}
