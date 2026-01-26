package com.isentric.bulkgateway.bg.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_smpp_response")
public class SMSMessageResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private int rowId;

    @Column(name = "guid", length = 50, nullable = false)
    private String guid = "";

    @Column(name = "status", length = 20, nullable = false)
    private String status = "";

    @Lob
    @Column(name = "message")
    private byte[] message;

    @Lob
    @Column(name = "value")
    private byte[] value;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "delete_flag", nullable = false)
    private int deleteFlag = 0;

}
