package com.br.api.library.api.resource;

import com.br.api.library.api.dto.BookDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create() {
        BookDto dto = new BookDto();

        dto.setAuthor("Autor");
        dto.setTitle("Meu livro");
        dto.setIsbn("123");
        dto.setId(12L);

        return dto;
    }
}
