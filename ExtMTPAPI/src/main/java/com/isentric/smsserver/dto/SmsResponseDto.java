package com.isentric.smsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponseDto {
    private int returnCode;
    private String messageId;
    private String msisdn;
    private String returnMsg;
    
    public SmsResponseDto(int returnCode, String returnMsg) {
        this.returnCode = returnCode;
        this.returnMsg = returnMsg;
    }
}

