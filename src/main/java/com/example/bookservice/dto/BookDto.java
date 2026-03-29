package com.example.bookservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record BookDto(
        Long id,
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,
        @NotBlank(message = "Author is required")
        @Size(max = 255, message = "Author must be at most 255 characters")
        String author,
        @NotBlank(message = "ISBN is required")
        @Size(max = 50, message = "ISBN must be at most 50 characters")
        String isbn,
        LocalDate publishedDate
) {
}
