package com.example.books.dto;

import java.time.LocalDateTime;

public record PurchaseResponce(
    Long id,
    String customerName,
    Long bookId,
    String bookTitle,
    Integer quantity,
    Double totalPrice,
    LocalDateTime purchasedAt
) {}
