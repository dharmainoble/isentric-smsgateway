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
    @Column(name = "cpidentity", length = 50)
    private String cpidentity;
    
    @Column(name = "shortcode", length = 10)
    private String shortcode;

    @Column(name = "cp_ip", length = 50)
    private String cpIp;
    
    @Column(name = "hlr_flag", length = 1)
    private String hlrFlag;
    

}

