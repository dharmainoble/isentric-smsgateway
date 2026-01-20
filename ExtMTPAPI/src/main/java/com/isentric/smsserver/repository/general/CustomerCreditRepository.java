package com.isentric.smsserver.repository.general;

import com.isentric.smsserver.model.general.CustomerCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CustomerCreditRepository extends JpaRepository<CustomerCredit, Long> {
    
    Optional<CustomerCredit> findByCustid(String custid);
    
    @Modifying
    @Query("UPDATE CustomerCredit c SET c.creditBalance = c.creditBalance - :amount, c.lastUpdated = CURRENT_TIMESTAMP WHERE c.custid = :custid AND c.creditBalance >= :amount")
    int deductCredit(@Param("custid") String custid, @Param("amount") BigDecimal amount);
    
    @Modifying
    @Query("UPDATE CustomerCredit c SET c.creditBalance = c.creditBalance + :amount, c.lastUpdated = CURRENT_TIMESTAMP WHERE c.custid = :custid")
    int addCredit(@Param("custid") String custid, @Param("amount") BigDecimal amount);
}

