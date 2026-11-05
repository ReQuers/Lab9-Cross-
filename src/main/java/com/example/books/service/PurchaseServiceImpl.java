package com.example.books.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.books.dto.CustomerTotalResponce;
import com.example.books.dto.PurchaseRequest;
import com.example.books.dto.PurchaseResponce;
import com.example.books.entity.Book;
import com.example.books.entity.Purchase;
import com.example.books.exception.PurchaseNotFoundException;
import com.example.books.repository.PurchaseRepository;

@Service 
public class PurchaseServiceImpl implements PurchaseService {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseServiceImpl.class);

    private final PurchaseRepository purchaseRepository;
    private final BookService bookService;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,BookService bookService){
        this.purchaseRepository = purchaseRepository;
        this.bookService = bookService;
    }

    @Override
    public PurchaseResponce createPurchase(PurchaseRequest request) {
        logger.info("Reques to create purchase:{}", request);

        Book book = bookService.getBookById(request.bookId());

        Purchase purchase = new Purchase();
        purchase.setCustomerName(request.customerName());
        purchase.setBook(book);
        purchase.setQuantity(request.quantity());
        purchase.setTotalPrice(book.getPrice()* request.quantity());
        purchase.setPurchasedAt(LocalDateTime.now());

        Purchase saved = purchaseRepository.save(purchase);
        return toResponce(saved);
    }
    @Override
    public List<PurchaseResponce> getAllPurcahses() {
        logger.info("Request to get all purchases");
        return purchaseRepository.findAll().stream().map(this::toResponce).toList();
    }

    @Override
    public List<PurchaseResponce> getPurcahseByCustomer(String customerName) {
        logger.info("Request to get purchases for customer = {}",customerName);
        
        return purchaseRepository.findByCustomerName(customerName).stream().map(this::toResponce).toList(); 
    }

    @Override
    public PurchaseResponce getPurchaseById(Long id) {
        logger.info("Request to get purchases for id = {}",id);
        
        Purchase purchase = purchaseRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Purchase with id = {} not found",id);
                return new PurchaseNotFoundException(id);
            });
        
        return toResponce(purchase);
    }

    @Override
    public void deletePurchase(Long id) {
        logger.info("Request to delete purchase id = {}",id);

        if(!purchaseRepository.existsById(id)){
            logger.warn("Cannot delete: Purchase with id={},not found",id);
            throw new PurchaseNotFoundException(id);
        }
        purchaseRepository.deleteById(id);
    }

    @Override
    public CustomerTotalResponce getCustomerTotal(String customerName) {
        logger.info("Request to get total spent by customer={}",customerName);
        Double totalSpent = purchaseRepository.sumTotalPriceByCustomer(customerName);
        return new CustomerTotalResponce(customerName,totalSpent);    
    }

    private PurchaseResponce toResponce(Purchase purchase){
        return new PurchaseResponce(
            purchase.getId(),
            purchase.getCustomerName(),
            purchase.getBook().getId(),
            purchase.getBook().getTitle(),
            purchase.getQuantity(),
            purchase.getTotalPrice(),
            purchase.getPurchasedAt()
        );
    }
}
