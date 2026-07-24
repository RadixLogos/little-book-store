package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.BookDTO;
import com.radixlogos.littlebookstore.dto.EditorDTO;
import com.radixlogos.littlebookstore.dto.GenreDTO;
import com.radixlogos.littlebookstore.dto.filter.BookFilterDTO;
import com.radixlogos.littlebookstore.entities.Book;
import com.radixlogos.littlebookstore.entities.Genre;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import com.radixlogos.littlebookstore.repositories.GenreRepository;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Transactional(readOnly = true)
    public Page<BookDTO> findAllBooks(Pageable pageable, BookFilterDTO bookFilter){
        if(
            !bookFilter.getName().isEmpty()
            && bookFilter.getEditorId() == null
            && bookFilter.getGenreId() == null){
            return bookRepository.findAllPagedByName(pageable,bookFilter.getName()).map(BookDTO::fromBook);
        } else if (
                !bookFilter.getName().isEmpty()
                && bookFilter.getEditorId() != null
                && bookFilter.getGenreId() == null) {
            return bookRepository.findAllPagedByNameEditor(pageable,bookFilter.getName(), bookFilter.getEditorId()).map(BookDTO::fromBook);
        } else if (
                !bookFilter.getName().isEmpty()
                && bookFilter.getEditorId() != null
                && bookFilter.getGenreId() != null) {
            return bookRepository.findAllPagedByNameEditorGenre(pageable,bookFilter.getName(), bookFilter.getEditorId(), bookFilter.getGenreId()).map(BookDTO::fromBook);
        } else if (
                !bookFilter.getName().isEmpty()
                && bookFilter.getEditorId() == null
                && bookFilter.getGenreId() != null) {
            return bookRepository.findAllPagedByNameGenre(pageable,bookFilter.getName(), bookFilter.getGenreId()).map(BookDTO::fromBook);
        }  else if (
                bookFilter.getName().isEmpty()
                && bookFilter.getEditorId() != null
                && bookFilter.getGenreId() != null) {
            return bookRepository.findAllPagedByEditorGenre(pageable,bookFilter.getEditorId(), bookFilter.getGenreId()).map(BookDTO::fromBook);

        } else if (
                bookFilter.getName().isEmpty()
                && bookFilter.getEditorId() == null
                && bookFilter.getGenreId() != null) {
            return bookRepository.findAllPagedByGenre(pageable,bookFilter.getGenreId()).map(BookDTO::fromBook);

        } else if (
                bookFilter.getName().isEmpty()
                && bookFilter.getEditorId() != null
                && bookFilter.getGenreId() == null) {
            return bookRepository.findAllPagedByEditor(pageable,bookFilter.getEditorId()).map(BookDTO::fromBook);

        } else {
            return bookRepository.findAll(pageable).map(BookDTO::fromBook);
        }

    }

    @Transactional(readOnly = true)
    public BookDTO findBookById(Long bookId){
        if(bookRepository.findById(bookId).isEmpty()){
            throw new ResourceNotFoundException("Livro não encontrado");
        }
        return BookDTO.fromBook(bookRepository.findById(bookId).get());
    }
    @Transactional
    public BookDTO insertBook(BookDTO bookDTO){
        var book = new Book();
        copyDTOToEntity(book,bookDTO);
        bookRepository.save(book);
        return BookDTO.fromBook(book);
    }

    @Transactional
    public BookDTO updateBook(Long bookId, BookDTO bookDTO){
        if(!bookRepository.existsById(bookId)){
            throw new ResourceNotFoundException("Livro não encontrado");
        }
        var book = bookRepository.findById(bookId).orElseThrow(()-> new ResourceNotFoundException("Livro não encontrado"));
        copyDTOToEntity(book,bookDTO);
        bookRepository.save(book);
        return BookDTO.fromBook(book);
    }

    @Transactional
    public void deleteBook(Long bookId){
        if(!bookRepository.existsById(bookId)){
            throw new ResourceNotFoundException("Livro não encontrado");
        }
        try{
        bookRepository.deleteById(bookId);

        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Falha de integridade referencial");
        }

    }

    private void copyDTOToEntity(Book book, BookDTO bookDTO){
        book.setName(bookDTO.name());
        book.setEditor(EditorDTO.fromEditorDTO(bookDTO.editor()));
        book.setAuthor(bookDTO.author());
        book.setDescription(bookDTO.description());
        book.setImgUrl(bookDTO.imgUrl());
        if(bookDTO.genres() != null){
            List<Long> genresIds = new ArrayList<>();
            for(GenreDTO genreDTO : bookDTO.genres()){
                Genre genre = genreRepository.getReferenceById(genreDTO.id());
                genre.addBooks(book);
                genresIds.add(genreDTO.id());
            }

            genreRepository.deleteWhenNotInUpdate(genresIds,bookDTO.id());
        }

        book.setPrice(bookDTO.price());
        book.setStockQuantity(bookDTO.stockQuantity());
        book.setImgUrl(bookDTO.imgUrl());
    }

}
