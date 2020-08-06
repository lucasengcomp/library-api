package com.br.api.library.service;

import com.br.api.library.api.exceptions.BusinessException;
import com.br.api.library.api.model.entity.Book;
import com.br.api.library.api.model.repositoy.BookRepository;
import com.br.api.library.service.impl.BookServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService service;

    @MockBean
    BookRepository repository;

    @BeforeEach
    public void setUp() {
        this.service = new BookServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um livrvo")
    public void saveBookTest() {

        //cenario
        Book book = createValidBook();
        Mockito.when(repository.save(book)).thenReturn(
                            Book.builder()
                            .id(1L)
                            .isbn("123")
                            .author("Immanuel Kant")
                            .title("Fundamentos da metafísica dos costumes").build()
                );

        //execução
        Book savedBook = service.save(book);

        //verificação
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getIsbn()).isEqualTo("123");
        assertThat(savedBook.getTitle()).isEqualTo("Fundamentos da metafísica dos costumes");
        assertThat(savedBook.getAuthor()).isEqualTo("Immanuel Kant");
    }

    private Book createValidBook() {
        return Book.builder().isbn("123").author("Immanuel Kant").title("Fundamentos da metafísica dos costumes").build();
    }

    @Test
    @DisplayName("Deve lançar erro de negócio ao tentar salvar um livro com ISBN duplicado.")
    public void shouldNotSaveBookWithDuplicatedIsbn() {
        //cenario
        Book book = createValidBook();
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(true);
        //verificação
        Throwable exception = Assertions.catchThrowable( () -> service.save(book));
        assertThat(exception)
                .isInstanceOf(BusinessException.class)
                .hasMessage("ISBN já está cadastrada!");

//        Mockito.verify(repository, Mockito.times(10)).save(book); //verifica que o método save foi executado 5 vezes

        //execução
        Mockito.verify(repository, Mockito.never()).save(book); //verifica que o método save não vai ser chamado

    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void getByIdTest() {
        Long id = 1L;

        Book book = createValidBook();
        book.setId(id);

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> foundBook = service.getById(id);

        assertThat(foundBook.isPresent()).isTrue();
        assertThat(foundBook.get().getId()).isEqualTo(id);
        assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
        assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());

    }

    @Test
    @DisplayName("Deve retornar vazio ao tentar obter um livro por id quando ele não existe na base")
    public void bookNotFoundByIdTest() {
        Long id = 1L;

        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> book = service.getById(id);

        assertThat(book.isPresent()).isFalse();
    }

}
