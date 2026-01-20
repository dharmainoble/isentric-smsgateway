package com.isentric.smsserver.controller;

import com.isentric.smsserver.service.CreditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/CheckSMSUserCredit")
@Slf4j
@RequiredArgsConstructor
public class CreditController {
    
    private final CreditService creditService;
    
    /**
     * Check customer credit balance
     */
    @GetMapping
    public ResponseEntity<String> checkCredit(
            @RequestParam(required = false) String custid) {
        
        log.info("Credit check request for customer: {}", custid);
        
        if (custid == null || custid.isEmpty()) {
            return ResponseEntity.badRequest().body("Customer ID required");
        }
        
        BigDecimal balance = creditService.getCreditBalance(custid);
        boolean hasCredit = creditService.hasCredit(custid);
        
        String response = String.format(
            "Customer: %s, Balance: %.2f, Has Credit: %s",
            custid, balance, hasCredit ? "YES" : "NO"
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Get detailed credit information (JSON response)
     */
    @GetMapping("/details")
    public ResponseEntity<?> getCreditDetails(@RequestParam String custid) {
        
        BigDecimal balance = creditService.getCreditBalance(custid);
        boolean hasCredit = creditService.hasCredit(custid);
        
        return ResponseEntity.ok(new CreditResponse(custid, balance, hasCredit));
    }
    
    // Inner DTO class
    record CreditResponse(String custid, BigDecimal balance, boolean hasCredit) {}
}

