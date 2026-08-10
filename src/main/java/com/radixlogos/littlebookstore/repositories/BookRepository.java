package com.radixlogos.littlebookstore.repositories;

import com.radixlogos.littlebookstore.dto.filter.BookFilterDTO;
import com.radixlogos.littlebookstore.entities.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    @Query(value = "SELECT * FROM tb_books ORDER BY stock_quantity desc , name asc" , nativeQuery = true)
    Page<Book> findAllPaged(Pageable pageable);

    @Query("SELECT obj FROM Book obj WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%',:bookName,'%')) ORDER BY obj.name asc, obj.stockQuantity desc")
    Page<Book> findAllPagedByName(Pageable pageable, String bookName);

    @Query(
            value = """
        SELECT *
        FROM tb_books b
        WHERE b.editor_id = :editorId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM tb_books b
        WHERE b.editor_id = :editorId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            nativeQuery = true
    )
    Page<Book> findAllPagedByEditor(Pageable pageable, Long editorId);

    @NativeQuery(value = """
            select * 
            from tb_books b 
            join tb_genre_book tgb 
            on tgb.book_id = b.id
            where tgb.genre_id = :genreId
            GROUP BY b.stock_quantity, b.name, b.id, tgb.genre_id, tgb.book_id
            ORDER BY b.stock_quantity desc , b.name asc
            """,countQuery = """
            select count(*) 
            from tb_books b 
            join tb_genre_book tgb 
            on tgb.book_id = b.id
            where tgb.genre_id = :genreId
            GROUP BY b.stock_quantity, b.name, b.id, tgb.genre_id, tgb.book_id
            ORDER BY b.stock_quantity desc , b.name asc
            """)
    Page<Book> findAllPagedByGenre(Pageable pageable, Long genreId);


    @Query(
            value = """
        SELECT *
        FROM tb_books b
        WHERE UPPER(b.name) LIKE UPPER(CONCAT(:bookName,'%'))  AND b.editor_id = :editorId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM tb_books b
        WHERE UPPER(b.name) LIKE UPPER(CONCAT(:bookName,'%')) AND b.editor_id = :editorId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            nativeQuery = true
    )
    Page<Book> findAllPagedByNameEditor(Pageable pageable, String bookName, Long editorId);

    @Query(
            value = """
        SELECT *
        FROM tb_books b
        JOIN tb_genre_book g
        ON g.book_id = b.id
        WHERE UPPER(b.name) LIKE UPPER(CONCAT('%',:bookName,'%'))  AND g.genre_id = :genreId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM tb_books b
        JOIN tb_genre_book g
        ON g.book_id = b.id
        WHERE UPPER(b.name) LIKE UPPER(CONCAT('%',:bookName,'%'))  AND g.genre_id = :genreId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            nativeQuery = true
    )
    Page<Book> findAllPagedByNameGenre(Pageable pageable, String bookName,  Long genreId);

    @Query(
            value = """
        SELECT *
        FROM tb_books b
        JOIN tb_genre_book g
        ON g.book_id = b.id
        WHERE b.editor_id = :editorId  AND g.genre_id = :genreId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM tb_books b
        JOIN tb_genre_book g
        ON g.book_id = b.id
        WHERE b.editor_id = :editorId  AND g.genre_id = :genreId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            nativeQuery = true
    )
    Page<Book> findAllPagedByEditorGenre(Pageable pageable, Long editorId,  Long genreId);

    @Query(
            value = """
        SELECT *
        FROM tb_books b
        JOIN tb_genre_book g
        ON g.book_id = b.id
        WHERE UPPER(b.name) LIKE UPPER(CONCAT('%',:bookName,'%'))  AND b.editor_id = :editorId AND g.genre_id = :genreId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM tb_books b
        JOIN tb_genre_book g
        ON g.book_id = b.id
        WHERE UPPER(b.name) LIKE UPPER(CONCAT('%',:bookName,'%')) AND b.editor_id = :editorId AND g.genre_id = :genreId
        ORDER BY b.stock_quantity desc , b.name asc
        """,
            nativeQuery = true
    )
    Page<Book> findAllPagedByNameEditorGenre(Pageable pageable, String bookName, Long editorId, Long genreId);





}
