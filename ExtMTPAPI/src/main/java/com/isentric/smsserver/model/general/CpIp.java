package com.isentric.smsserver.model.general;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cpip", schema = "bulk_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpIp {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private Long rowId;
    
    @Column(name = "shortcode", length = 10)
    private String shortcode;
    
    @Column(name = "cpidentity", length = 50)
    private String cpidentity;
    
    @Column(name = "cp_ip", length = 50)
    private String cpIp;
    
    @Column(name = "hlr_flag", length = 1)
    private String hlrFlag;
    
    @Column(name = "active", length = 1)
    private String active;
}

