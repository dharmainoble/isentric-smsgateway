package com.isentric.smsserver.service;

import com.isentric.smsserver.model.general.CustomerCredit;
import com.isentric.smsserver.repository.general.CustomerCreditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditService {
    
    private final CustomerCreditRepository customerCreditRepository;
    
    /**
     * Check if customer has sufficient credit (cached)
     */
    // Temporarily disabled cache for debugging
    // @Cacheable(value = "creditCache", key = "#custid")
    public boolean hasCredit(String custid) {
        try {
            log.info("Checking credit for customer: {}", custid);
            Optional<CustomerCredit> creditOpt = customerCreditRepository.findByCustid(custid);
            
            if (creditOpt.isEmpty()) {
                log.warn("Credit not found for customer: {}", custid);
                return false;
            }
            
            CustomerCredit credit = creditOpt.get();
            log.info("Credit found - Customer: {}, Balance: {}, Active: {}",
                    custid, credit.getCreditBalance(), credit.getActive());

            boolean hasCredit = credit.getCreditBalance() != null &&
                               credit.getCreditBalance().compareTo(BigDecimal.ZERO) > 0 &&
                               "1".equals(credit.getActive());
            
            log.info("Credit check result for {}: {} (Balance: {}, Active: {})",
                    custid, hasCredit, credit.getCreditBalance(), credit.getActive());
            return hasCredit;
            
        } catch (Exception e) {
            log.error("Error checking credit for customer: {}", custid, e);
            return false;
        }
    }
    
    /**
     * Get customer credit balance
     */
    @Cacheable(value = "creditCache", key = "'balance_' + #custid")
    public BigDecimal getCreditBalance(String custid) {
        return customerCreditRepository.findByCustid(custid)
                .map(CustomerCredit::getCreditBalance)
                .orElse(BigDecimal.ZERO);
    }
    
    /**
     * Deduct credit from customer account
     */
    @Transactional("generalTransactionManager")
    @CacheEvict(value = "creditCache", key = "#custid")
    public boolean deductCredit(String custid, BigDecimal amount) {
        try {
            log.info("Deducting credit - Customer: {}, Amount: {}", custid, amount);
            
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("Invalid deduction amount: {}", amount);
                return false;
            }
            
            int updated = customerCreditRepository.deductCredit(custid, amount);
            
            if (updated > 0) {
                log.info("Credit deducted successfully - Customer: {}, Amount: {}", custid, amount);
                return true;
            } else {
                log.warn("Insufficient credit for customer: {}", custid);
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error deducting credit for customer: {}", custid, e);
            return false;
        }
    }
    
    /**
     * Add credit to customer account (refund/topup)
     */
    @Transactional("generalTransactionManager")
    @CacheEvict(value = "creditCache", key = "#custid")
    public boolean addCredit(String custid, BigDecimal amount) {
        try {
            log.info("Adding credit - Customer: {}, Amount: {}", custid, amount);
            
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("Invalid credit amount: {}", amount);
                return false;
            }
            
            int updated = customerCreditRepository.addCredit(custid, amount);
            
            if (updated > 0) {
                log.info("Credit added successfully - Customer: {}, Amount: {}", custid, amount);
                return true;
            } else {
                log.warn("Failed to add credit for customer: {}", custid);
                return false;
            }
            
        } catch (Exception e) {
            log.error("Error adding credit for customer: {}", custid, e);
            return false;
        }
    }
}

