package com.isentric.bulkgateway.util;

/**
 * Example Usage of TgaSoapUtil Static Utility Class
 * This class demonstrates how to use the static TGA SOAP service utility
 */
public class TgaSoapUtilExample {

    /**
     * Example 1: Simple synchronous call
     */
    public static void example1_SimpleSyncCall() {
        try {
            String tgaUrl = "http://localhost:8080/TGAService";
            String msisdn = "601234567890";

            // Call TGA service with custom URL
            String response = TgaSoapUtil.callTga(msisdn, tgaUrl);
            System.out.println("TGA Response: " + response);

            // Extract telco from response
            String telco = TgaSoapUtil.extractTelcoFromResponse(response);
            System.out.println("Telco: " + telco);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 2: Using default URL
     */
    public static void example2_DefaultUrl() {
        try {
            String msisdn = "601234567890";

            // Call TGA service with default URL
            String response = TgaSoapUtil.callTga(msisdn);
            System.out.println("TGA Response: " + response);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 3: Asynchronous call with callback
     */
    public static void example3_AsyncCall() {
        String tgaUrl = "http://localhost:8080/TGAService";
        String msisdn = "601234567890";

        // Call TGA service asynchronously
        TgaSoapUtil.callTgaAsync(msisdn, tgaUrl, new TgaSoapUtil.TgaResponseCallback() {
            @Override
            public void onSuccess(String response) {
                System.out.println("Success! Response: " + response);
                String telco = TgaSoapUtil.extractTelcoFromResponse(response);
                System.out.println("Extracted Telco: " + telco);
            }

            @Override
            public void onError(Exception exception) {
                System.err.println("Error occurred: " + exception.getMessage());
                exception.printStackTrace();
            }
        });

        // Continue with other operations while TGA call is being processed
        System.out.println("TGA call initiated asynchronously...");
    }

    /**
     * Example 4: Check response validity
     */
    public static void example4_CheckResponse() {
        try {
            String tgaUrl = "http://localhost:8080/TGAService";
            String msisdn = "601234567890";

            String response = TgaSoapUtil.callTga(msisdn, tgaUrl);

            // Check if response is successful
            if (TgaSoapUtil.isSuccessResponse(response)) {
                System.out.println("✓ Response is successful");
                String telco = TgaSoapUtil.extractTelcoFromResponse(response);
                System.out.println("Telco: " + telco);
            } else {
                System.out.println("✗ Response indicates error");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 5: Using in a loop (batch processing)
     */
    public static void example5_BatchProcessing() {
        String[] msisdnList = {
            "601234567890",
            "601234567891",
            "601234567892"
        };

        String tgaUrl = "http://localhost:8080/TGAService";

        for (String msisdn : msisdnList) {
            try {
                System.out.println("Processing MSISDN: " + msisdn);
                String response = TgaSoapUtil.callTga(msisdn, tgaUrl);

                if (TgaSoapUtil.isSuccessResponse(response)) {
                    String telco = TgaSoapUtil.extractTelcoFromResponse(response);
                    System.out.println("  → Telco: " + telco);
                } else {
                    System.out.println("  → Error processing MSISDN");
                }

            } catch (Exception e) {
                System.err.println("  → Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Example 6: Using in a Spring Service (existing code)
     */
    public static class ExampleSpringService {

        public String getTelcoInfo(String msisdn) throws Exception {
            // Can use static util without Spring dependency
            String tgaUrl = "http://localhost:8080/TGAService";
            String response = TgaSoapUtil.callTga(msisdn, tgaUrl);
            return TgaSoapUtil.extractTelcoFromResponse(response);
        }
    }

    /**
     * Main method to run examples
     */
    public static void main(String[] args) {
        System.out.println("=== TGA SOAP Util Examples ===\n");

        // Uncomment the example you want to run:
        // example1_SimpleSyncCall();
        // example2_DefaultUrl();
        // example3_AsyncCall();
        // example4_CheckResponse();
        // example5_BatchProcessing();

        System.out.println("\nExamples ready to use. Uncomment in main() to run.");
    }
}

