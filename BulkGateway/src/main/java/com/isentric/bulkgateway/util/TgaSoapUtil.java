package com.isentric.bulkgateway.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * Static Utility Class for TGA (Telecom Gateway API) SOAP Service
 * This class provides static methods to call TGA service without Spring dependency injection
 * Can be used anywhere in the application without Spring context
 */
public class TgaSoapUtil {

    private static final String DEFAULT_TGA_URL = "http://localhost:8080/TGAService";
    private static final RestTemplate restTemplate = new RestTemplate();

    private TgaSoapUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Call TGA service with MSISDN using default URL
     *
     * @param msisdn Mobile number to query
     * @return SOAP response from TGA service
     * @throws Exception if service call fails
     */
    public static String callTga(String msisdn) throws Exception {
        return callTga(msisdn, DEFAULT_TGA_URL);
    }

    /**
     * Call TGA service with MSISDN and custom URL
     *
     * @param msisdn Mobile number to query
     * @param tgaUrl TGA service URL
     * @return SOAP response from TGA service
     * @throws Exception if service call fails
     */
    public static String callTga(String msisdn, String tgaUrl) throws Exception {
        if (msisdn == null || msisdn.trim().isEmpty()) {
            throw new IllegalArgumentException("MSISDN cannot be null or empty");
        }

        if (tgaUrl == null || tgaUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("TGA URL cannot be null or empty");
        }

        String soapRequest = buildSoapRequest(msisdn);
        HttpHeaders headers = buildSoapHeaders();
        HttpEntity<String> request = new HttpEntity<>(soapRequest, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    tgaUrl,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                throw new RuntimeException("TGA service returned status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("TGA service call failed: " + e.getMessage());
            throw new Exception("Failed to call TGA service: " + e.getMessage(), e);
        }
    }

    /**
     * Build SOAP request for TGA query
     *
     * @param msisdn Mobile number to query
     * @return SOAP XML request string
     */
    private static String buildSoapRequest(String msisdn) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "
                + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<soapenv:Body>"
                + "<ns1:queryTGA soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" "
                + "xmlns:ns1=\"urn:webservice.qs.mnp.isentric.com\">"
                + "<in0 xsi:type=\"soapenc:string\" "
                + "xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">"
                + msisdn
                + "</in0>"
                + "</ns1:queryTGA>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
    }

    /**
     * Build HTTP headers for SOAP request
     *
     * @return HTTP headers with SOAP content type
     */
    private static HttpHeaders buildSoapHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/xml; charset=utf-8"));
        headers.add("SOAPAction", "");
        return headers;
    }

    /**
     * Call TGA service asynchronously (non-blocking)
     *
     * @param msisdn Mobile number to query
     * @param tgaUrl TGA service URL
     * @param callback Callback interface to handle response
     */
    public static void callTgaAsync(String msisdn, String tgaUrl, TgaResponseCallback callback) {
        new Thread(() -> {
            try {
                String response = callTga(msisdn, tgaUrl);
                callback.onSuccess(response);
            } catch (Exception e) {
                callback.onError(e);
            }
        }).start();
    }

    /**
     * Parse SOAP response to extract telco information
     *
     * @param soapResponse SOAP response string
     * @return Extracted telco value or null if not found
     */
    public static String extractTelcoFromResponse(String soapResponse) {
        if (soapResponse == null || soapResponse.isEmpty()) {
            return null;
        }

        try {
            // Look for telco value in SOAP response
            int startIndex = soapResponse.indexOf("<telco>");
            int endIndex = soapResponse.indexOf("</telco>");

            if (startIndex != -1 && endIndex != -1) {
                return soapResponse.substring(startIndex + 7, endIndex);
            }

            // Alternative tag search
            startIndex = soapResponse.indexOf("<resp_telco>");
            endIndex = soapResponse.indexOf("</resp_telco>");

            if (startIndex != -1 && endIndex != -1) {
                return soapResponse.substring(startIndex + 12, endIndex);
            }

            return null;
        } catch (Exception e) {
            System.err.println("Error parsing SOAP response: " + e.getMessage());
            return null;
        }
    }

    /**
     * Check if SOAP response indicates success
     *
     * @param soapResponse SOAP response string
     * @return true if response contains success indicators
     */
    public static boolean isSuccessResponse(String soapResponse) {
        if (soapResponse == null || soapResponse.isEmpty()) {
            return false;
        }

        return !soapResponse.contains("faultstring")
                && !soapResponse.contains("faultcode")
                && soapResponse.contains("soapenv:Body");
    }

    /**
     * Callback interface for async TGA calls
     */
    public interface TgaResponseCallback {
        void onSuccess(String response);

        void onError(Exception exception);
    }
}

