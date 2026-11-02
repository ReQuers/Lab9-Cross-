package com.example.books.config;

import com.example.books.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataInitializerTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private DataInitializer initializer;

    @Test
    void run_whenDbEmpty_shouldSeedData() throws Exception {
        when(repository.count()).thenReturn(0L);

        initializer.run();

        verify(repository).saveAll(anyList());
    }

    @Test
    void run_whenDbHasData_shouldDoNothing() throws Exception {
        when(repository.count()).thenReturn(5L);

        initializer.run();

        verify(repository, never()).saveAll(anyList());
    }
}
