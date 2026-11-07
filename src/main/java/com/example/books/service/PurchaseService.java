package com.example.books.service;

import com.example.books.dto.PurchaseResponce;
import com.example.books.dto.CustomerTotalResponce;
import com.example.books.dto.PurchaseRequest;

import java.util.List;

public interface PurchaseService {
    PurchaseResponce createPurchase(PurchaseRequest request);
    List<PurchaseResponce> getAllPurcahses();
    List<PurchaseResponce> getPurcahseByCustomer(String customerName);
    PurchaseResponce getPurchaseById(Long id);
    void deletePurchase(Long id);
    CustomerTotalResponce getCustomerTotal(String customerName);
}
