package com.br.api.library.model.repository;

import com.br.api.library.api.model.entity.Book;
import com.br.api.library.api.model.repositoy.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest //cria uma instancia no bd em memoria para executar os testes somente em tempo de execução -> H2 DB
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager; //criar um cenário para teste

    @Autowired
    BookRepository repository;

    private Book createNewBook(String isbn) {
        return Book.builder().title("Aventuras").author("Lucas").isbn(isbn).build();
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um livro na base com o ISBN informado.")
    public void returnTrueWhenIsbnExisits() {
        //cenário
        String isbn = "123";
        Book book = createNewBook(isbn);
        entityManager.persist(book);

        //execução
        boolean exists = repository.existsByIsbn(isbn);

        //verificação
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando não existir um livro na base com o ISBN informado.")
    public void returnFalseWhenIsbnDoesntExisit() {
        //cenário
        String isbn = "123";

        //execução
        boolean exists = repository.existsByIsbn(isbn);

        //verificação
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um livro por id")
    public void findByIdTest() {
        //cenario
        Book book = createNewBook("123");
        entityManager.persist(book);

        //execução
        Optional<Book> foundBook = repository.findById(book.getId());

        //verificaçaõ
        assertThat(foundBook.isPresent()).isTrue();
    }

}
