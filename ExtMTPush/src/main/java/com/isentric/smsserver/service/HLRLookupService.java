package com.isentric.smsserver.service;

import com.isentric.smsserver.object.HLRResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HLRLookupService {

    private static final Logger logger = LoggerFactory.getLogger(HLRLookupService.class);

    /**
     * Perform HLR lookup for a given MSISDN
     */
    public HLRResponse lookupMSISDN(String msisdn) {
        logger.info("Performing HLR lookup for: {}", msisdn);

        HLRResponse response = new HLRResponse();
        response.setMsisdn(msisdn);

        try {
            // TODO: Implement actual HLR lookup via external service

            // Simulate response for now
            response.setStatus("VALID");
            response.setNetworkCode(getNetworkCodeFromMsisdn(msisdn));
            response.setCountryCode("60"); // Malaysia
            response.setPorted("false");
            response.setRoaming("false");

        } catch (Exception e) {
            logger.error("Error performing HLR lookup: {}", e.getMessage(), e);
            response.setStatus("ERROR");
            response.setErrorCode("HLR001");
            response.setErrorMessage(e.getMessage());
        }

        return response;
    }

    /**
     * Batch HLR lookup
     */
    public HLRResponse[] batchLookup(String[] msisdns) {
        logger.info("Performing batch HLR lookup for {} MSISDNs", msisdns.length);

        HLRResponse[] responses = new HLRResponse[msisdns.length];

        for (int i = 0; i < msisdns.length; i++) {
            responses[i] = lookupMSISDN(msisdns[i]);
        }

        return responses;
    }

    /**
     * Check if MSISDN is valid
     */
    public boolean isValidMSISDN(String msisdn) {
        HLRResponse response = lookupMSISDN(msisdn);
        return "VALID".equals(response.getStatus());
    }

    /**
     * Get network operator from MSISDN
     */
    public String getNetworkOperator(String msisdn) {
        String networkCode = getNetworkCodeFromMsisdn(msisdn);

        switch (networkCode) {
            case "50212":
            case "50217":
                return "MAXIS";
            case "50216":
            case "50214":
                return "DIGI";
            case "50219":
            case "50213":
            case "50210":
            case "50211":
                return "CELCOM";
            case "50218":
                return "UMOBILE";
            default:
                return "UNKNOWN";
        }
    }

    private String getNetworkCodeFromMsisdn(String msisdn) {
        if (msisdn == null || msisdn.length() < 5) {
            return "UNKNOWN";
        }

        // Remove country code if present
        String localNumber = msisdn;
        if (msisdn.startsWith("60")) {
            localNumber = msisdn.substring(2);
        } else if (msisdn.startsWith("+60")) {
            localNumber = msisdn.substring(3);
        }

        // Determine network based on prefix
        if (localNumber.startsWith("12") || localNumber.startsWith("17")) {
            return "50212"; // Maxis
        } else if (localNumber.startsWith("16") || localNumber.startsWith("14")) {
            return "50216"; // Digi
        } else if (localNumber.startsWith("19") || localNumber.startsWith("13") ||
                   localNumber.startsWith("10") || localNumber.startsWith("11")) {
            return "50219"; // Celcom
        } else if (localNumber.startsWith("18")) {
            return "50218"; // U Mobile
        }

        return "UNKNOWN";
    }
}

