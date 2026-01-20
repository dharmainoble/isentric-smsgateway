package com.isentric.smsserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HlrLookupRequestDto {
    private String shortcode;
    private String custid;
    private String msisdn;
    private String requestId;
}

