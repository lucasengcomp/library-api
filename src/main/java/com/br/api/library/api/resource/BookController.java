package com.br.api.library.api.resource;

import com.br.api.library.api.dto.BookDTO;
import com.br.api.library.api.exceptions.ApiErrors;
import com.br.api.library.api.model.entity.Book;
import com.br.api.library.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService service;
    private ModelMapper modelMapper;

    public BookController(BookService service, ModelMapper mapper) {
        this.service = service;
        this.modelMapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody @Valid BookDTO dto) {

        Book entity = modelMapper.map(dto, Book.class);

        entity = service.save(entity);

        return modelMapper.map(entity, BookDTO.class);
    }

    /*
    * Lança um erro quando os dados são informados errados
    * */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        return new ApiErrors(bindingResult);
    }
}
