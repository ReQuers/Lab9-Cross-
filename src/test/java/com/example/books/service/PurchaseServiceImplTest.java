package com.example.books.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.books.dto.PurchaseRequest;
import com.example.books.dto.PurchaseResponce;
import com.example.books.entity.Book;
import com.example.books.entity.Purchase;
import com.example.books.repository.PurchaseRepository;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceImplTest {
    @Mock 
    private PurchaseRepository purchaseRepository;
    @Mock 
    private BookService bookService;
    @InjectMocks 
    private PurchaseServiceImpl purchaseServiceImpl;
    
    @Test 
    void createPurchase_shouldCalculateTotalPriceOnServer(){
        Book book = new Book("Clean Code","Marthin","IT",250.0);
        book.setId(1L);
        when(bookService.getBookById(1L)).thenReturn(book);
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(inv -> inv.getArgument(0));
        
        
        PurchaseResponce result = purchaseServiceImpl.createPurchase(new PurchaseRequest("Ivan",1L, 3));

        ArgumentCaptor<Purchase> captor = ArgumentCaptor.forClass(Purchase.class);
        verify(purchaseRepository).save(captor.capture());
        assertThat(captor.getValue().getTotalPrice()).isEqualTo(750.0);
        assertThat(captor.getValue().getPurchasedAt()).isNotNull();
        assertThat(result.totalPrice()).isEqualTo(750.0);
        assertThat(result.bookTitle()).isEqualTo("Clean Code");

    }
}
