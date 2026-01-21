package com.radixlogos.littlebookstore.services;

import com.radixlogos.littlebookstore.dto.BookDTO;
import com.radixlogos.littlebookstore.entities.Book;
import com.radixlogos.littlebookstore.repositories.BookRepository;
import com.radixlogos.littlebookstore.services.exceptions.BookException;
import com.radixlogos.littlebookstore.services.exceptions.BookNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Page<BookDTO> findAllBooks(Pageable pageable, String name){
        return bookRepository.findAllPaged(pageable, name).map(BookDTO::fromBook);
    }
    @Transactional(readOnly = true)
    public BookDTO findBookById(Long bookId){
        if(bookRepository.findById(bookId).isEmpty()){
            throw new BookNotFoundException("Livro não encontrado");
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
            throw new BookNotFoundException("Livro não encontrado");
        }
        var book = bookRepository.getReferenceById(bookId);
        copyDTOToEntity(book,bookDTO);
        bookRepository.save(book);
        return BookDTO.fromBook(book);
    }

    @Transactional
    public void deleteBook(Long bookId){
        if(!bookRepository.existsById(bookId)){
            throw new BookNotFoundException("Livro não encontrado");
        }
        try{
        bookRepository.deleteById(bookId);

        }catch (DataIntegrityViolationException e){
            throw new BookException("Falha de integridade referencial");
        }

    }

    private void copyDTOToEntity(Book book, BookDTO bookDTO){
        book.setName(bookDTO.name());
        book.setEditor(bookDTO.editor());
        book.setPrice(bookDTO.price());
        book.setStockQuantity(bookDTO.stockQuantity());
    }
}
