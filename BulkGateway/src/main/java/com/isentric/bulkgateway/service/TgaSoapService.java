package com.isentric.bulkgateway.service;


import com.isentric.bulkgateway.util.TgaSoapUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * TGA SOAP Service - Spring Service wrapper for TgaSoapUtil
 * Uses the static utility class for actual SOAP calls
 */
@Service
@Lazy
public class TgaSoapService {

    private static final Logger logger = Logger.getLogger(TgaSoapService.class);

    @Value("${tga.url:urn:webservice.qs.mnp.isentric.com}")
    private String tgaUrl;

    /**
     * Call TGA SOAP service for the given MSISDN
     *
     * @param msisdn Mobile number to query
     * @return SOAP response from TGA service
     * @throws Exception if service call fails
     */
    public String callTga(String msisdn) throws Exception {
        try {
            if (tgaUrl == null || tgaUrl.isEmpty()) {
                logger.warn("TGA URL not configured, using default");
                tgaUrl = "urn:webservice.qs.mnp.isentric.com";
            }
            logger.debug("Calling TGA with MSISDN: " + msisdn + " using URL: " + tgaUrl);
            String response = TgaSoapUtil.callTga(msisdn, tgaUrl);
            logger.debug("TGA response received for MSISDN: " + msisdn);
            return response;
        } catch (Exception e) {
            logger.error("Error calling TGA service for MSISDN: " + msisdn, e);
            throw e;
        }
    }

    /**
     * Call TGA service asynchronously
     *
     * @param msisdn Mobile number to query
     * @param callback Callback to handle response
     */
    public void callTgaAsync(String msisdn, TgaSoapUtil.TgaResponseCallback callback) {
        TgaSoapUtil.callTgaAsync(msisdn, tgaUrl, callback);
    }

    /**
     * Extract telco from SOAP response
     *
     * @param soapResponse SOAP response string
     * @return Telco name or null
     */
    public String extractTelcoFromResponse(String soapResponse) {
        return TgaSoapUtil.extractTelcoFromResponse(soapResponse);
    }

    /**
     * Check if SOAP response is successful
     *
     * @param soapResponse SOAP response string
     * @return true if successful
     */
    public boolean isSuccessResponse(String soapResponse) {
        return TgaSoapUtil.isSuccessResponse(soapResponse);
    }
}

