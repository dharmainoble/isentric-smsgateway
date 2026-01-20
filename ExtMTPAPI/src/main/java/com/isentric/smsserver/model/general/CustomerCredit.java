package com.isentric.smsserver.model.general;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_credit", schema = "bulk_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCredit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private Long rowId;
    
    @Column(name = "custid", length = 50, unique = true)
    private String custid;
    
    @Column(name = "credit_balance", precision = 10, scale = 2)
    private BigDecimal creditBalance;
    
    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @Column(name = "active", length = 1)
    private String active;
}

