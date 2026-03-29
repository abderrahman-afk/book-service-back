package com.example.bookservice.repository;

import com.example.bookservice.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void saveShouldPersistBook() {
        Book book = new Book(null, "Spring in Action", "Craig Walls", "9781617297571", LocalDate.of(2021, 9, 14));

        Book savedBook = bookRepository.save(book);

        assertThat(savedBook.getId()).isNotNull();
        assertThat(bookRepository.findById(savedBook.getId())).isPresent();
    }
}
