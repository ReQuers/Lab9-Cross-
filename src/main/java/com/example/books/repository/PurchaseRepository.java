package com.example.books.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.books.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase,Long>{
    List<Purchase> findByCustomerName(String customerName);
    
    boolean existsByBookId(Long bookId);

    @Query ("select coalesce(sum(p.totalPrice),0.0) from Purchase p where p.customerName =:customer")
    Double sumTotalPriceByCustomer(@Param("customer") String customer);
}
