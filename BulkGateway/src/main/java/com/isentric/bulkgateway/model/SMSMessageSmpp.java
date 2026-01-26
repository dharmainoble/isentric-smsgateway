package com.isentric.bulkgateway.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_smpp_in")
public class SMSMessageSmpp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private int rowId;

    @Column(name = "guid", length = 50, nullable = false)
    private String guid = "";

    @Column(name = "groupId", length = 50, nullable = false)
    private String groupId = "";

    @Column(name = "ip", length = 10, nullable = false)
    private String ip = "";

    @Column(name = "smsc", length = 10, nullable = false)
    private String smsc = "";

    @Column(name = "telco", length = 50, nullable = false)
    private String telco = "";

    @Column(name = "smppName", length = 50, nullable = false)
    private String smppName = "";

    @Column(name = "smppConfig", length = 50, nullable = false)
    private String smppConfig = "";

    @Column(name = "moid", length = 50, nullable = false)
    private String moid = "";

    @Column(name = "sender", length = 15, nullable = false)
    private String sender = "";

    @Column(name = "recipient", length = 15, nullable = false)
    private String recipient = "";

    @Column(name = "senderType", nullable = false)
    private int senderType = 0;

    @Column(name = "keyword", nullable = false)
    private String keyword = "";

    @Column(name = "price", length = 10, nullable = false)
    private String price = "";

    @Column(name = "messageType", nullable = false)
    private int messageType = 0;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "status", nullable = false)
    private int status = 0;

    @Column(name = "flag", nullable = false)
    private int flag = 0;

    @Column(name = "error", length = 200, nullable = false)
    private String error = "";

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "message")
    private String message;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "callbackURL")
    private String callbackURL;

    @Column(name = "userGroup", length = 50, nullable = false)
    private String userGroup = "";

    @Column(name = "credit", length = 10)
    private String credit = "0.00";

    @Column(name = "cflag")
    private Integer cFlag = 0;

    @Transient
    private String shortcode;

    @Transient
    public String messageBytes;

    @Transient
    public String property;

    @Transient
    public int type;

}
