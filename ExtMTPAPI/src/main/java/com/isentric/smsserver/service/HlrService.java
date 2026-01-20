package com.isentric.smsserver.service;

import com.isentric.smsserver.util.SmsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class HlrService {
    
    /**
     * Perform HLR lookup (cached)
     */
    @Cacheable(value = "hlrCache", key = "#msisdn")
    public HlrResponse performHlrLookup(String msisdn, String custid, String requestId) {
        try {
            log.info("HLR Lookup - MSISDN: {}, Customer: {}, RequestID: {}", msisdn, custid, requestId);
            
            // Normalize MSISDN
            String normalizedMsisdn = SmsUtil.normalizeMsisdn(msisdn);
            
            // Validate MSISDN
            if (!SmsUtil.isValidMsisdn(normalizedMsisdn)) {
                return new HlrResponse(requestId, msisdn, "INVALID", "Invalid MSISDN format", null, null);
            }
            
            // Detect carrier
            String carrier = SmsUtil.getTelco(normalizedMsisdn);
            
            // TODO: Implement actual HLR lookup via carrier API
            // For now, return simulated response based on telco detection
            
            HlrResponse response = new HlrResponse(
                requestId,
                normalizedMsisdn,
                "ACTIVE",
                "Subscriber active",
                carrier,
                SmsUtil.isLocalMsisdn(normalizedMsisdn) ? "MY" : "INTL"
            );
            
            log.info("HLR Lookup completed - MSISDN: {}, Carrier: {}, Status: {}", 
                     normalizedMsisdn, carrier, response.status());
            
            return response;
            
        } catch (Exception e) {
            log.error("Error performing HLR lookup for MSISDN: {}", msisdn, e);
            return new HlrResponse(requestId, msisdn, "ERROR", e.getMessage(), null, null);
        }
    }
    
    /**
     * HLR Response record
     */
    public record HlrResponse(
        String requestId,
        String msisdn,
        String status,
        String message,
        String carrier,
        String networkType
    ) {
        public Map<String, String> toMap() {
            Map<String, String> map = new HashMap<>();
            map.put("requestId", requestId);
            map.put("msisdn", msisdn);
            map.put("status", status);
            map.put("message", message);
            map.put("carrier", carrier);
            map.put("networkType", networkType);
            return map;
        }
    }
}

