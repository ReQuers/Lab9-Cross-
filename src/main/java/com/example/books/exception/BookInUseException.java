package com.example.books.exception;

public class BookInUseException extends RuntimeException {
    public BookInUseException(Long id){
        super("Book with id:"+id+" can`t be removed: Has purchase");
    }
}
