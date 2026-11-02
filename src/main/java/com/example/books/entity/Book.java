package com.example.books.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Назва книги не може бути порожньою")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Автор не може бути порожнім")
    @Column(nullable = false)
    private String author;

    private String category;

    @NotNull(message = "Ціна обов'язкова")
    @Positive(message = "Ціна має бути більшою за 0")
    @Column(nullable = false)
    private Double price;

    public Book() {
    }

    public Book(String title, String author, String category, Double price) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{id=" + id + ", title='" + title + "', author='" + author
                + "', category='" + category + "', price=" + price + "}";
    }
}
