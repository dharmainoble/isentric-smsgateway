package com.isentric.smsserver.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequestDto implements Serializable {
    
    @NotBlank(message = "Shortcode is required")
    private String shortcode;
    
    @NotBlank(message = "Customer ID is required")
    private String custid;
    
    @NotBlank(message = "Recipient MSISDN is required")
    private String rmsisdn;

    @NotBlank(message = "Message ID is required")
    private String mtid;

    private String password;
    
    private String mtprice;
    
    private String productCode;
    
    private Integer productType;
    
    private String keyword;
    
    private Integer dataEncoding;
    
    private String dataStr;
    
    private String dataUrl;
    
    private Integer dnRep;
    
    private String groupTag;
    
    private String urlTitle;
    
    private String ewigFlag;
    
    private String cFlag;
}

