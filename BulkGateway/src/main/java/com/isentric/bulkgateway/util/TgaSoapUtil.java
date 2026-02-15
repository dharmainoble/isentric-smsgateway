package com.isentric.bulkgateway.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * Static Utility Class for TGA SOAP Service
 * Provides static methods to call TGA service without Spring dependency injection
 * Can be used anywhere in the application without Spring context
 */
public class TgaSoapUtil {

    // Default TGA URL - must be a valid HTTP/HTTPS endpoint
    // Example: http://tga-service.example.com/soap or https://webservice.qs.mnp.isentric.com/service
    private static final String DEFAULT_TGA_URL = "http://webservice.qs.mnp.isentric.com/soap";
    private static final RestTemplate restTemplate = new RestTemplate();

    private TgaSoapUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Call TGA SOAP service with MSISDN using default URL
     *
     * @param msisdn Mobile number to query
     * @return SOAP response body
     * @throws Exception if service call fails
     */
    public static String callTga(String msisdn) throws Exception {
        return callTga(msisdn, DEFAULT_TGA_URL);
    }

    /**
     * Call TGA SOAP service with MSISDN and custom URL
     *
     * @param msisdn Mobile number to query
     * @param tgaUrl TGA service URL (must be HTTP/HTTPS)
     * @return SOAP response body
     * @throws Exception if service call fails
     */
    public static String callTga(String msisdn, String tgaUrl) throws Exception {
        if (msisdn == null || msisdn.trim().isEmpty()) {
            throw new IllegalArgumentException("MSISDN cannot be null or empty");
        }

        if (tgaUrl == null || tgaUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("TGA URL cannot be null or empty");
        }

        // Validate URL format
        if (!tgaUrl.startsWith("http://") && !tgaUrl.startsWith("https://")) {
            throw new IllegalArgumentException(
                    "TGA URL must start with http:// or https://. " +
                    "Invalid URL: " + tgaUrl + ". " +
                    "Do NOT use URN format (urn:...). " +
                    "Example correct format: http://webservice.qs.mnp.isentric.com/soap"
            );
        }

        try {
            String soapRequest = buildSoapRequest(msisdn);
            HttpHeaders headers = buildSoapHeaders();
            HttpEntity<String> request = new HttpEntity<>(soapRequest, headers);

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
        headers.add("SOAPAction", ""); // Important for Axis
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

