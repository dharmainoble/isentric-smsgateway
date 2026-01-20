package com.isentric.smsserver.model.avatar;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "extmt_mtid", schema = "extmt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtMtId {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id")
    private Long rowId;
    
    @Column(name = "mtid", length = 100, unique = true)
    private String mtid;
    
    @Column(name = "custid", length = 50)
    private String custid;
    
    @Column(name = "date")
    private LocalDateTime date;
}

