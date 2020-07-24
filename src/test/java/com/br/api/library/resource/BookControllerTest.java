package com.br.api.library.resource;

import com.br.api.library.api.dto.BookDTO;
import com.br.api.library.api.exceptions.BusinessException;
import com.br.api.library.api.model.entity.Book;
import com.br.api.library.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)//cria um mini contexto para rodar os testes
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc//configura o teste para receber requisições
public class BookControllerTest {

    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;

    @MockBean
    BookService service;
    @Test
    @DisplayName("Deve criar um livro com sucesso")
    public void createBookTest() throws Exception {

        BookDTO dto = BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
        Book savedBook = Book.builder().id(101L).author("Artur").title("As aventuras").isbn("001").build();

        BDDMockito.given(service.save(Mockito.any(Book.class))).willReturn(savedBook);
        String json = new ObjectMapper().writeValueAsString(dto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("title").value(dto.getTitle()))
                .andExpect(jsonPath("author ").value(dto.getAuthor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()));
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para criação de livro.")
    public void createInvalidBookTest() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Deve lançar um erro ao tentar cadastrar um livro com ISBN já utilizado por outro livro")
    public void createBookWithDuplicateIsbn() throws Exception {

        BookDTO dto = createNewBook();

        String json = new ObjectMapper().writeValueAsString(dto);

        String mensagemErro = "ISBN já está cadastrada!";

        BDDMockito.given(service.save(Mockito.any(Book.class)))
                .willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));

        BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();

    }


    @Test
    @DisplayName("Deve obter as informações do livro")
    public void getBookDetailTest() throws Exception {
        //cenario
        Long id = 1L;

        Book book = Book.builder()
                .id(id)
                .title(createNewBook().getTitle())
                .author(createNewBook().getAuthor())
                .isbn(createNewBook().getIsbn())
                .build();

        BDDMockito.given(service.getById(id)).willReturn(Optional.of(book));

        //execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/"+id))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("author").value(createNewBook().getAuthor()))
                .andExpect(jsonPath("title").value(createNewBook().getTitle()))
                .andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));

    }

    public BookDTO createNewBook() {
        return BookDTO.builder().author("Artur").title("As aventuras").isbn("001").build();
    }

}
