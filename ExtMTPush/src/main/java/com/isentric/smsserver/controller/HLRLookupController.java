package com.isentric.smsserver.controller;

import com.isentric.smsserver.object.HLRResponse;
import com.isentric.smsserver.service.HLRLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for HLR Lookup operations
 */
@RestController
@RequestMapping("/api/hlr")
public class HLRLookupController {

    private static final Logger logger = LoggerFactory.getLogger(HLRLookupController.class);

    private final HLRLookupService hlrLookupService;

    @Autowired
    public HLRLookupController(HLRLookupService hlrLookupService) {
        this.hlrLookupService = hlrLookupService;
    }

    /**
     * Single MSISDN lookup
     */
    @GetMapping("/lookup/{msisdn}")
    public ResponseEntity<HLRResponse> lookupMSISDN(@PathVariable String msisdn) {
        logger.info("HLR lookup request for: {}", msisdn);

        HLRResponse response = hlrLookupService.lookupMSISDN(msisdn);

        if ("VALID".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Batch MSISDN lookup
     */
    @PostMapping("/batch")
    public ResponseEntity<HLRResponse[]> batchLookup(@RequestBody String[] msisdns) {
        logger.info("Batch HLR lookup request for {} MSISDNs", msisdns.length);

        HLRResponse[] responses = hlrLookupService.batchLookup(msisdns);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get network operator
     */
    @GetMapping("/network/{msisdn}")
    public ResponseEntity<Map<String, Object>> getNetworkOperator(@PathVariable String msisdn) {
        logger.info("Network operator lookup for: {}", msisdn);

        Map<String, Object> response = new HashMap<>();
        String operator = hlrLookupService.getNetworkOperator(msisdn);

        response.put("msisdn", msisdn);
        response.put("operator", operator);

        return ResponseEntity.ok(response);
    }

    /**
     * Validate MSISDN
     */
    @GetMapping("/validate/{msisdn}")
    public ResponseEntity<Map<String, Object>> validateMSISDN(@PathVariable String msisdn) {
        logger.info("MSISDN validation request for: {}", msisdn);

        Map<String, Object> response = new HashMap<>();
        boolean isValid = hlrLookupService.isValidMSISDN(msisdn);

        response.put("msisdn", msisdn);
        response.put("valid", isValid);

        return ResponseEntity.ok(response);
    }
}

