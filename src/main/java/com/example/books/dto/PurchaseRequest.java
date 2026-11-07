package com.example.books.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PurchaseRequest (
    @NotBlank (message = "The Name can`t be empty") String customerName,
    @NotNull (message = "Required bookId") Long bookId,
    @NotNull (message = "RequiredQuantity")
    @Min (value = 1,message = "Quantity must be bigger than 0") Integer quantity
) {}

