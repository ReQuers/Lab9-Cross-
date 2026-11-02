package com.example.books.controller;

import com.example.books.entity.Book;
import com.example.books.exception.BookNotFoundException;
import com.example.books.exception.GlobalExceptionHandler;
import com.example.books.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getBooks_shouldReturnList() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of(book(1L, "Clean Code"), book(2L, "Effective Java")));

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Clean Code"));
    }

    @Test
    void getBooks_withAuthorParam_shouldFilterByAuthor() throws Exception {
        when(bookService.getBooksByAuthor("Martin")).thenReturn(List.of(book(1L, "Clean Code")));

        mockMvc.perform(get("/api/books").param("author", "Martin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getBook_whenExists_shouldReturnBook() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(book(1L, "Clean Code"));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"));
    }

    @Test
    void getBook_whenNotFound_shouldReturn404() throws Exception {
        when(bookService.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        mockMvc.perform(get("/api/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createBook_whenValid_shouldReturn201() throws Exception {
        when(bookService.saveBook(any(Book.class))).thenReturn(book(1L, "Clean Code"));

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"Clean Code","author":"Martin","category":"IT","price":650.0}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createBook_whenInvalid_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"","author":"","price":-5}
                                """))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).saveBook(any(Book.class));
    }

    @Test
    void updateBook_shouldReturnUpdatedBook() throws Exception {
        when(bookService.updateBook(any(Long.class), any(Book.class))).thenReturn(book(1L, "New Title"));

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title":"New Title","author":"Martin","category":"IT","price":700.0}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"));
    }

    @Test
    void deleteBook_shouldReturn204() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }

    private Book book(Long id, String title) {
        Book book = new Book(title, "Martin", "IT", 650.0);
        book.setId(id);
        return book;
    }
}
