package com.example.bookservice.service;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.entity.Book;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book savedBook = bookRepository.save(toEntity(bookDto));
        return toDto(savedBook);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return toDto(findBook(id));
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book existingBook = findBook(id);
        existingBook.setTitle(bookDto.title());
        existingBook.setAuthor(bookDto.author());
        existingBook.setIsbn(bookDto.isbn());
        existingBook.setPublishedDate(bookDto.publishedDate());

        Book updatedBook = bookRepository.save(existingBook);
        return toDto(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book existingBook = findBook(id);
        bookRepository.delete(existingBook);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    private BookDto toDto(Book book) {
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedDate()
        );
    }

    private Book toEntity(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.title());
        book.setAuthor(bookDto.author());
        book.setIsbn(bookDto.isbn());
        book.setPublishedDate(bookDto.publishedDate());
        return book;
    }
}
