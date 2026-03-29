package com.example.bookservice.service;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book(1L, "Clean Code", "Robert C. Martin", "9780132350884", LocalDate.of(2008, 8, 1));
        bookDto = new BookDto(null, "Clean Code", "Robert C. Martin", "9780132350884", LocalDate.of(2008, 8, 1));
    }

    @Test
    void createBookShouldReturnSavedBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto result = bookService.createBook(bookDto);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.title()).isEqualTo("Clean Code");
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void getAllBooksShouldReturnBookDtos() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDto> result = bookService.getAllBooks();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).isbn()).isEqualTo("9780132350884");
    }

    @Test
    void getBookByIdShouldReturnBookWhenFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDto result = bookService.getBookById(1L);

        assertThat(result.author()).isEqualTo("Robert C. Martin");
    }

    @Test
    void getBookByIdShouldThrowWhenMissing() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessage("Book not found with id: 99");
    }

    @Test
    void updateBookShouldOverwriteFields() {
        BookDto updatedDto = new BookDto(null, "Refactoring", "Martin Fowler", "9780201485677", LocalDate.of(1999, 7, 8));
        Book updatedBook = new Book(1L, "Refactoring", "Martin Fowler", "9780201485677", LocalDate.of(1999, 7, 8));

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(updatedBook);

        BookDto result = bookService.updateBook(1L, updatedDto);

        assertThat(result.title()).isEqualTo("Refactoring");
        assertThat(result.author()).isEqualTo("Martin Fowler");
    }

    @Test
    void deleteBookShouldRemoveExistingBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(book);
    }
}
