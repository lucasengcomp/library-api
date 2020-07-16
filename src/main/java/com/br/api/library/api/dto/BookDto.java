package com.br.api.library.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDto {

    private Long id;

    private String title;

    private String author;

    private String isbn;
}
