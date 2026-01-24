package com.isentric.bulkgateway.bc.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bulk_destination_sms")
public class BulkDestinationSMS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "custid")
    private String custid;

    @Column(name = "local_flag")
    private String localFlag;

    @Column(name = "int_flag")
    private String intFlag;

    @Column(name = "created_date")
    private LocalDateTime createdAt;

}
