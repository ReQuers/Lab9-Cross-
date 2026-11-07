package com.example.books.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.books.dto.CustomerTotalResponce;
import com.example.books.dto.PurchaseRequest;
import com.example.books.dto.PurchaseResponce;
import com.example.books.service.PurchaseService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/purchases")
public class PurchaseController {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService){
        this.purchaseService = purchaseService;
    }
    
    @GetMapping 
    public List<PurchaseResponce> getPurchases(@RequestParam(name = "customer",required = false) String customer){
        logger.debug("GET /api/purcahses customer={}",customer);
        if(customer != null && !customer.isBlank()){
            return purchaseService.getPurcahseByCustomer(customer);
        }
        return purchaseService.getAllPurcahses();
    }
    @GetMapping("/total")
    public CustomerTotalResponce getCustomerTotal(@RequestParam (name = "customer") String customer){
        logger.debug("GET /api/purchases/total customer ={}",customer);
        return purchaseService.getCustomerTotal(customer);
    }
    @GetMapping ("/{id}")
    public PurchaseResponce getPurchase(@PathVariable("id")Long id){
        logger.debug("GET /api/purchases/{}",id);

        return purchaseService.getPurchaseById(id);
    }
    @PostMapping 
    public ResponseEntity<PurchaseResponce> createPurchase( @Valid @RequestBody PurchaseRequest request){
        logger.debug("POST /api/purcahses {}",request);

        PurchaseResponce created = purchaseService.createPurchase(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable("id")Long id){
        logger.debug("DELETE /api/purchases/{}",id);

        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }
    
}
