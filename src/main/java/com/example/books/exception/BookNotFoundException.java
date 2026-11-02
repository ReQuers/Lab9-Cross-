package com.example.books.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("Книгу з id=" + id + " не знайдено");
    }
}
