package com.br.api.library.service.impl;

import com.br.api.library.api.model.entity.Book;
import com.br.api.library.api.model.repositoy.BookRepository;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements com.br.api.library.service.BookService {

    private BookRepository repository;

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
    }

    public Book save(Book book) {
        return repository.save(book);
    }
}
