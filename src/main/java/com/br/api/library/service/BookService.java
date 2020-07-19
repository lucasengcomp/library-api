package com.br.api.library.service;

import com.br.api.library.api.model.entity.Book;
import org.springframework.stereotype.Service;

@Service
public interface BookService {

    Book save(Book any);
}
