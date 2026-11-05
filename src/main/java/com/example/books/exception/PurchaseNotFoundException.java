package com.example.books.exception;

public class PurchaseNotFoundException extends RuntimeException {
    public PurchaseNotFoundException(Long id){
        super("Purchase with id:"+id+" not found");
    }
}
