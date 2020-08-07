package com.br.api.library.service.impl;

import com.br.api.library.api.exceptions.BusinessException;
import com.br.api.library.api.model.entity.Book;
import com.br.api.library.api.model.repositoy.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImpl implements com.br.api.library.service.BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Book save(Book book) {
        if (repository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("ISBN já está cadastrada!");
        }
        return repository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public void delete(Book book) {
        if (book.getId() == null || book == null) {
            throw new IllegalArgumentException("Book id cant be null!");
        }
        this.repository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if (book.getId() == null || book == null) {
            throw new IllegalArgumentException("Book id cant be null!");
        }
        return  this.repository.save(book);
    }

}
