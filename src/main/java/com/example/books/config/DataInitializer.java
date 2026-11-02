package com.example.books.config;

import com.example.books.entity.Book;
import com.example.books.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    private static final String SEED_FILE = "data/books.csv";

    private final BookRepository bookRepository;

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() > 0) {
            logger.info("Database already contains books. Skip seeding.");
            return;
        }

        List<Book> books = loadBooksFromCsv();
        if (!books.isEmpty()) {
            bookRepository.saveAll(books);
            logger.info("Seeded {} books from {}", books.size(), SEED_FILE);
        }
    }

    private List<Book> loadBooksFromCsv() throws IOException {
        List<Book> books = new ArrayList<>();
        ClassPathResource resource = new ClassPathResource(SEED_FILE);

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }

                String[] columns = line.split(";", -1);
                if (columns.length < 4) {
                    continue;
                }

                String title = columns[0].trim();
                String author = columns[1].trim();
                String category = columns[2].trim();
                String priceText = columns[3].trim();

                Double price = Double.parseDouble(priceText);
                books.add(new Book(title, author, category, price));
            }
        }

        return books;
    }
}
