package com.isentric.smsserver.model.avatar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "extmtpush_receive_bulk", schema = "extmt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtMtPushReceive {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private Long rowId;
    
    @Column(name = "bill_flag", length = 1)
    private String billFlag;
    
    @Column(name = "process_flag", length = 1)
    private String processFlag;
    
    @Column(name = "shortcode", length = 10)
    private String shortcode;
    
    @Column(name = "custid", length = 50)
    private String custid;
    
    @Column(name = "rmsisdn", length = 20)
    private String rmsisdn;
    
    @Column(name = "smsisdn", length = 20)
    private String smsisdn;
    
    @Column(name = "mtid", length = 100)
    private String mtid;
    
    @Column(name = "mtprice", length = 10)
    private String mtprice;
    
    @Column(name = "product_type")
    private Integer productType;
    
    @Column(name = "product_code", length = 50)
    private String productCode;
    
    @Column(name = "keyword", length = 255)
    private String keyword;
    
    @Column(name = "data_encoding")
    private Integer dataEncoding;
    
    @Column(name = "data_str", columnDefinition = "TEXT")
    private String dataStr;
    
    @Column(name = "data_url", length = 500)
    private String dataUrl;
    
    @Column(name = "url_title", length = 255)
    private String urlTitle;
    
    @Column(name = "dnrep")
    private Integer dnrep;
    
    @Column(name = "group_tag", length = 50)
    private String groupTag;
    
    @Column(name = "ewig_flag", length = 1)
    private String ewigFlag;
    
    @Column(name = "received_date")
    private LocalDateTime receivedDate;
    
    @Column(name = "sent_date")
    private LocalDateTime sentDate;
    
    @Column(name = "delivery_status", length = 20)
    private String deliveryStatus;
}

