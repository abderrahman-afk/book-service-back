package com.example.bookservice.controller;

import com.example.bookservice.dto.BookDto;
import com.example.bookservice.exception.BookNotFoundException;
import com.example.bookservice.exception.GlobalExceptionHandler;
import com.example.bookservice.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(GlobalExceptionHandler.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void createBookShouldReturnCreated() throws Exception {
        BookDto request = new BookDto(null, "Domain-Driven Design", "Eric Evans", "9780321125217", LocalDate.of(2003, 8, 30));
        BookDto response = new BookDto(1L, "Domain-Driven Design", "Eric Evans", "9780321125217", LocalDate.of(2003, 8, 30));

        when(bookService.createBook(any(BookDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Domain-Driven Design"));
    }

    @Test
    void getAllBooksShouldReturnList() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(
                new BookDto(1L, "Effective Java", "Joshua Bloch", "9780134685991", LocalDate.of(2018, 1, 6))
        ));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].author").value("Joshua Bloch"));
    }

    @Test
    void getBookByIdShouldReturnNotFoundWhenMissing() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book not found with id: 99"));
    }

    @Test
    void updateBookShouldReturnUpdatedBook() throws Exception {
        BookDto request = new BookDto(null, "Test Driven Development", "Kent Beck", "9780321146533", LocalDate.of(2002, 11, 8));
        BookDto response = new BookDto(1L, "Test Driven Development", "Kent Beck", "9780321146533", LocalDate.of(2002, 11, 8));

        when(bookService.updateBook(eq(1L), any(BookDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("9780321146533"));
    }

    @Test
    void deleteBookShouldReturnNoContent() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createBookShouldReturnBadRequestForInvalidPayload() throws Exception {
        BookDto invalidRequest = new BookDto(null, "", "Kent Beck", "", LocalDate.of(2002, 11, 8));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.title").value("Title is required"))
                .andExpect(jsonPath("$.validationErrors.isbn").value("ISBN is required"));
    }
}
