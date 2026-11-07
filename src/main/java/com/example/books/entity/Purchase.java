package com.example.books.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Audited.Table;

import jakarta.persistence.*;

@Entity 
@Table(name = "purchases")
public class Purchase {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private long id;

    @Column(nullable = false)
    private String customerName;

    @ManyToOne (optional = false)
    @JoinColumn (name = "book_id",nullable = false)
    private Book book;

    @Column (nullable = false)
    private Integer quantity;

    @Column (nullable = false)
    private Double totalPrice;

    @Column (nullable = false)
    private LocalDateTime purchasedAt;

    public Purchase(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}
