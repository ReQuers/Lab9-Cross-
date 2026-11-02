package com.example.books.service;

import com.example.books.entity.Book;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();

    Book getBookById(Long id);

    List<Book> getBooksByAuthor(String author);

    Book saveBook(Book book);

    Book updateBook(Long id, Book newData);

    void deleteBook(Long id);
}
