package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.exception.BookInUseException;
import com.example.books.exception.BookNotFoundException;
import com.example.books.repository.BookRepository;
import com.example.books.repository.PurchaseRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final PurchaseRepository purchaseRepository;
    public BookServiceImpl(BookRepository bookRepository,PurchaseRepository purchaseRepository) {
        this.bookRepository = bookRepository;
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        logger.info("Request to get all Books");
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        logger.info("Request to get Book by id={}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Book with id={} not found", id);
                    return new BookNotFoundException(id);
                });
    }

    @Override
    public List<Book> getBooksByAuthor(String author) {
        logger.info("Request to get Books by author={}", author);
        return bookRepository.findByAuthor(author);
    }

    @Override
    public Book saveBook(Book book) {
        logger.info("Request to save Book {}", book);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long id, Book newData) {
        logger.info("Request to update Book id={}", id);
        Book existing = getBookById(id);
        existing.setTitle(newData.getTitle());
        existing.setAuthor(newData.getAuthor());
        existing.setCategory(newData.getCategory());
        existing.setPrice(newData.getPrice());
        return bookRepository.save(existing);
    }

    @Override
    public void deleteBook(Long id) {
        logger.info("Request to delete Book id={}", id);
        if (!bookRepository.existsById(id)) {
            logger.warn("Cannot delete: Book with id={} not found", id);
            throw new BookNotFoundException(id);
        }
        if(purchaseRepository.existsByBookId(id)){
            logger.warn("Cannot delete: Book with id={} exists");
            throw new BookInUseException(id);
        }
        bookRepository.deleteById(id);
    }
}
