package com.example.books.service;

import com.example.books.entity.Book;
import com.example.books.exception.BookNotFoundException;
import com.example.books.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllBooks_shouldReturnList() {
        when(repository.findAll()).thenReturn(genBookList());

        List<Book> result = bookService.getAllBooks();

        assertThat(result).hasSize(2);
    }

    @Test
    void getBookById_whenExists_shouldReturnBook() {
        when(repository.findById(1L)).thenReturn(Optional.of(createBook(1L, "Кобзар", "Шевченко", 250.0)));

        Book result = bookService.getBookById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Кобзар");
    }

    @Test
    void getBookById_whenNotFound_shouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void getBooksByAuthor_shouldReturnBooksOfAuthor() {
        when(repository.findByAuthor("Шевченко")).thenReturn(List.of(createBook(1L, "Кобзар", "Шевченко", 250.0)));

        List<Book> result = bookService.getBooksByAuthor("Шевченко");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthor()).isEqualTo("Шевченко");
    }

    @Test
    void saveBook_shouldReturnSavedBook() {
        Book toSave = new Book("Clean Code", "Martin", "IT", 650.0);
        when(repository.save(toSave)).thenReturn(createBook(1L, "Clean Code", "Martin", 650.0));

        Book result = bookService.saveBook(toSave);

        assertThat(result.getId()).isEqualTo(1L);
        verify(repository).save(toSave);
    }

    @Test
    void updateBook_whenExists_shouldUpdateFields() {
        Book existing = createBook(1L, "Old", "Old Author", 100.0);
        Book newData = new Book("New", "New Author", "IT", 200.0);
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.updateBook(1L, newData);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New");
        assertThat(result.getPrice()).isEqualTo(200.0);
    }

    @Test
    void updateBook_whenNotFound_shouldThrowAndNotSave() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(99L, new Book("A", "B", "C", 1.0)))
                .isInstanceOf(BookNotFoundException.class);

        verify(repository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_whenExists_shouldDelete() {
        when(repository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteBook_whenNotFound_shouldThrowAndNotDelete() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook(99L))
                .isInstanceOf(BookNotFoundException.class);

        verify(repository, never()).deleteById(anyLong());
    }

    private List<Book> genBookList() {
        return List.of(
                createBook(1L, "Кобзар", "Шевченко", 250.0),
                createBook(2L, "Лісова пісня", "Українка", 150.0));
    }

    private Book createBook(Long id, String title, String author, double price) {
        Book book = new Book(title, author, "Test", price);
        book.setId(id);
        return book;
    }
}
