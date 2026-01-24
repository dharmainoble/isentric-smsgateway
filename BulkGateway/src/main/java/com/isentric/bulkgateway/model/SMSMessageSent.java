package com.isentric.bulkgateway.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_smpp_sent")
public class SMSMessageSent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private Integer rowId;

    @Column(name = "guid", length = 50)
    private String guid;

    @Column(name = "groupId", length = 50)
    private String groupId;

    @Column(name = "telco", length = 50)
    private String telco;

    @Column(name = "smppName", length = 50)
    private String smppName;

    @Column(name = "smppId", length = 100)
    private String smppId;

    @Column(name = "moid", length = 50)
    private String moid;

    @Column(name = "sender", length = 50)
    private String sender;

    @Column(name = "recipient", length = 50)
    private String recipient;

    @Column(name = "senderType")
    private Integer senderType;

    @Column(name = "date")
    private LocalDateTime date;

    @Lob
    @Column(name = "message")
    private String message;

    @Column(name = "shortcode", length = 50)
    private String shortcode;

    @Column(name = "userGroup", length = 50)
    private String userGroup;

    @Column(name = "keyword", length = 255)
    private String keyword;

    @Column(name = "message_sequence")
    private Integer messageSequence;

    @Column(name = "credit", length = 10)
    private String credit;

    @Column(name = "price", length = 20)
    private String price;

    @Column(name = "smppType", length = 50)
    private String smppType;

    @Column(name = "smppStatus", length = 50)
    private String smppStatus;

    @Column(name = "timestamp", length = 50)
    private String timestamp;

    @Lob
    @Column(name = "bytes")
    private String bytes;

    @Column(name = "transactionId", length = 100)
    private String transactionId;

    @Column(name = "error", length = 200)
    private String error;

}

