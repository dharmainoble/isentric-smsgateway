package com.isentric.smsserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Test Controller for testing all ExtMTPush APIs
 * Access at: http://localhost:8087/api/test
 */
@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class ApiTestController {

    private static final Logger logger = LoggerFactory.getLogger(ApiTestController.class);

    /**
     * Get all available API endpoints
     */
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getApiList() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "ExtMTPush Spring Boot");
        response.put("version", "1.0.0");
        response.put("timestamp", System.currentTimeMillis());

        Map<String, Object> apis = new LinkedHashMap<>();

        // SMS APIs
        Map<String, String> smsApis = new LinkedHashMap<>();
        smsApis.put("POST /api/sms/send", "Send single SMS");
        smsApis.put("POST /api/sms/bulk/send", "Send bulk SMS");
        smsApis.put("GET /api/sms/status/{mtid}", "Check delivery status");
        smsApis.put("GET /api/sms/credit/{custid}", "Get user credit");
        smsApis.put("POST /api/sms/credit/deduct", "Deduct credit");
        smsApis.put("GET /api/sms/health", "Health check");
        apis.put("SMS", smsApis);

        // HLR APIs
        Map<String, String> hlrApis = new LinkedHashMap<>();
        hlrApis.put("GET /api/hlr/lookup/{msisdn}", "Single HLR lookup");
        hlrApis.put("POST /api/hlr/batch", "Batch HLR lookup");
        hlrApis.put("GET /api/hlr/network/{msisdn}", "Get network operator");
        hlrApis.put("GET /api/hlr/validate/{msisdn}", "Validate MSISDN");
        apis.put("HLR", hlrApis);

        // DN APIs
        Map<String, String> dnApis = new LinkedHashMap<>();
        dnApis.put("POST /dn/celcom", "Celcom delivery notification");
        dnApis.put("GET /dn/celcom", "Celcom delivery notification (GET)");
        dnApis.put("POST /dn/digi", "Digi delivery notification");
        dnApis.put("POST /dn/maxis", "Maxis delivery notification");
        dnApis.put("POST /dn/silverstreet", "SilverStreet delivery notification");
        dnApis.put("GET /dn/{operator}", "Generic delivery notification");
        apis.put("DeliveryNotification", dnApis);

        // Test APIs
        Map<String, String> testApis = new LinkedHashMap<>();
        testApis.put("GET /api/test", "List all APIs");
        testApis.put("GET /api/test/sms/send", "Test send SMS");
        testApis.put("GET /api/test/hlr/{msisdn}", "Test HLR lookup");
        testApis.put("GET /api/test/dn/celcom", "Test Celcom DN");
        testApis.put("GET /api/test/all", "Run all tests");
        apis.put("Test", testApis);

        response.put("apis", apis);

        return ResponseEntity.ok(response);
    }

    /**
     * Test Send SMS API
     */
    @GetMapping("/sms/send")
    public ResponseEntity<Map<String, Object>> testSendSms(
            @RequestParam(defaultValue = "60123456789") String msisdn,
            @RequestParam(defaultValue = "TEST001") String custid,
            @RequestParam(defaultValue = "Test message from ExtMTPush") String message) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("test", "Send SMS");
        response.put("endpoint", "POST /api/sms/send");

        Map<String, Object> sampleRequest = new LinkedHashMap<>();
        sampleRequest.put("shortcode", "12345");
        sampleRequest.put("custid", custid);
        sampleRequest.put("rmsisdn", msisdn);
        sampleRequest.put("smsisdn", "SENDER");
        sampleRequest.put("mtid", "MSG" + System.currentTimeMillis());
        sampleRequest.put("price", "000");
        sampleRequest.put("dataStr", message);
        sampleRequest.put("keyword", "TEST");

        response.put("sampleRequest", sampleRequest);
        response.put("curlCommand", "curl -X POST http://localhost:8087/api/sms/send " +
                "-H 'Content-Type: application/json' " +
                "-d '{\"shortcode\":\"12345\",\"custid\":\"" + custid + "\",\"rmsisdn\":\"" + msisdn + "\"," +
                "\"smsisdn\":\"SENDER\",\"mtid\":\"MSG001\",\"price\":\"000\",\"dataStr\":\"" + message + "\"}'");

        logger.info("Test Send SMS - msisdn: {}, custid: {}", msisdn, custid);

        return ResponseEntity.ok(response);
    }

    /**
     * Test Bulk SMS API
     */
    @GetMapping("/sms/bulk")
    public ResponseEntity<Map<String, Object>> testBulkSms() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("test", "Send Bulk SMS");
        response.put("endpoint", "POST /api/sms/bulk/send");

        Map<String, Object> sampleRequest = new LinkedHashMap<>();
        sampleRequest.put("billFlag", "1");
        sampleRequest.put("shortcode", "12345");
        sampleRequest.put("custid", "TEST001");
        sampleRequest.put("rmsisdn", "60123456789");
        sampleRequest.put("smsisdn", "SENDER");
        sampleRequest.put("mtid", "BULK" + System.currentTimeMillis());
        sampleRequest.put("mtprice", "000");
        sampleRequest.put("productType", 4);
        sampleRequest.put("dataEncoding", 0);
        sampleRequest.put("dataStr", "Test bulk message");
        sampleRequest.put("dnrep", 1);

        response.put("sampleRequest", sampleRequest);
        response.put("curlCommand", "curl -X POST http://localhost:8087/api/sms/bulk/send " +
                "-H 'Content-Type: application/json' " +
                "-d '{\"shortcode\":\"12345\",\"custid\":\"TEST001\",\"rmsisdn\":\"60123456789\"," +
                "\"smsisdn\":\"SENDER\",\"mtprice\":\"000\",\"productType\":4,\"dataStr\":\"Test bulk message\"}'");

        return ResponseEntity.ok(response);
    }

    /**
     * Test Check Delivery Status API
     */
    @GetMapping("/sms/status")
    public ResponseEntity<Map<String, Object>> testCheckStatus(
            @RequestParam(defaultValue = "MSG001") String mtid) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("test", "Check Delivery Status");
        response.put("endpoint", "GET /api/sms/status/{mtid}");
        response.put("mtid", mtid);
        response.put("curlCommand", "curl http://localhost:8087/api/sms/status/" + mtid);

        return ResponseEntity.ok(response);
    }

    /**
     * Test Get Credit API
     */
    @GetMapping("/sms/credit")
    public ResponseEntity<Map<String, Object>> testGetCredit(
            @RequestParam(defaultValue = "TEST001") String custid) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("test", "Get User Credit");
        response.put("endpoint", "GET /api/sms/credit/{custid}");
        response.put("custid", custid);
        response.put("curlCommand", "curl http://localhost:8087/api/sms/credit/" + custid);

        return ResponseEntity.ok(response);
    }

    /**
     * Test HLR Lookup API
     */
    @GetMapping("/hlr/{msisdn}")
    public ResponseEntity<Map<String, Object>> testHlrLookup(@PathVariable String msisdn) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("test", "HLR Lookup");
        response.put("endpoint", "GET /api/hlr/lookup/{msisdn}");
        response.put("msisdn", msisdn);
        response.put("curlCommand", "curl http://localhost:8087/api/hlr/lookup/" + msisdn);

        // Add sample response
        Map<String, Object> sampleResponse = new LinkedHashMap<>();
        sampleResponse.put("msisdn", msisdn);
        sampleResponse.put("status", "VALID");
        sampleResponse.put("networkCode", "50212");
        sampleResponse.put("countryCode", "60");
        sampleResponse.put("ported", "false");
        sampleResponse.put("roaming", "false");
        response.put("sampleResponse", sampleResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Test Celcom DN API
     */
    @GetMapping("/dn/celcom")
    public ResponseEntity<Map<String, Object>> testCelcomDn() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("test", "Celcom Delivery Notification");
        response.put("endpoint", "POST /dn/celcom");

        Map<String, String> params = new LinkedHashMap<>();
        params.put("SMP_Txid", "TX123456");
        params.put("SUB_Mobtel", "60123456789");
        params.put("APIType", "HTTP");
        params.put("SMP_Keyword", "TEST");
        params.put("SMP_ServiceID", "SVC001");
        params.put("DNStatus", "DELIVERED");
        params.put("SMS_SourceAddr", "12345");
        params.put("ErrorCode", "DeliveredToTerminal");

        response.put("parameters", params);
        response.put("curlCommand", "curl -X POST 'http://localhost:8087/dn/celcom?" +
                "SMP_Txid=TX123456&SUB_Mobtel=60123456789&APIType=HTTP&SMP_Keyword=TEST&" +
                "SMP_ServiceID=SVC001&DNStatus=DELIVERED&SMS_SourceAddr=12345&ErrorCode=DeliveredToTerminal'");

        return ResponseEntity.ok(response);
    }

    /**
     * Test Digi DN API
     */
    @GetMapping("/dn/digi")
    public ResponseEntity<Map<String, Object>> testDigiDn() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("test", "Digi Delivery Notification");
        response.put("endpoint", "POST /dn/digi");

        Map<String, String> params = new LinkedHashMap<>();
        params.put("messageId", "MSG123456");
        params.put("status", "DELIVERED");
        params.put("msisdn", "60163456789");
        params.put("deliveryTime", "2026-01-06 10:30:00");
        params.put("errorCode", "0");

        response.put("parameters", params);
        response.put("curlCommand", "curl -X POST 'http://localhost:8087/dn/digi?" +
                "messageId=MSG123456&status=DELIVERED&msisdn=60163456789&deliveryTime=2026-01-06+10:30:00&errorCode=0'");

        return ResponseEntity.ok(response);
    }

    /**
     * Test Maxis DN API
     */
    @GetMapping("/dn/maxis")
    public ResponseEntity<Map<String, Object>> testMaxisDn() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("test", "Maxis Delivery Notification");
        response.put("endpoint", "POST /dn/maxis");

        Map<String, String> params = new LinkedHashMap<>();
        params.put("messageId", "MSG789012");
        params.put("status", "DELIVRD");
        params.put("msisdn", "60173456789");
        params.put("deliveryTime", "2026-01-06 11:00:00");
        params.put("errorCode", "0");

        response.put("parameters", params);
        response.put("curlCommand", "curl -X POST 'http://localhost:8087/dn/maxis?" +
                "messageId=MSG789012&status=DELIVRD&msisdn=60173456789&deliveryTime=2026-01-06+11:00:00&errorCode=0'");

        return ResponseEntity.ok(response);
    }

    /**
     * Run all tests
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> runAllTests() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("title", "ExtMTPush API Test Suite");
        response.put("timestamp", System.currentTimeMillis());

        Map<String, Object> tests = new LinkedHashMap<>();

        // Health Check Test
        Map<String, Object> healthTest = new LinkedHashMap<>();
        healthTest.put("name", "Health Check");
        healthTest.put("endpoint", "GET /api/sms/health");
        healthTest.put("curl", "curl http://localhost:8087/api/sms/health");
        healthTest.put("expectedStatus", 200);
        tests.put("1_health", healthTest);

        // Send SMS Test
        Map<String, Object> sendTest = new LinkedHashMap<>();
        sendTest.put("name", "Send SMS");
        sendTest.put("endpoint", "POST /api/sms/send");
        sendTest.put("curl", "curl -X POST http://localhost:8087/api/sms/send -H 'Content-Type: application/json' " +
                "-d '{\"shortcode\":\"12345\",\"custid\":\"TEST001\",\"rmsisdn\":\"60123456789\"," +
                "\"smsisdn\":\"SENDER\",\"dataStr\":\"Test message\"}'");
        sendTest.put("expectedStatus", 200);
        tests.put("2_sendSms", sendTest);

        // Check Status Test
        Map<String, Object> statusTest = new LinkedHashMap<>();
        statusTest.put("name", "Check Delivery Status");
        statusTest.put("endpoint", "GET /api/sms/status/MSG001");
        statusTest.put("curl", "curl http://localhost:8087/api/sms/status/MSG001");
        statusTest.put("expectedStatus", 200);
        tests.put("3_checkStatus", statusTest);

        // Get Credit Test
        Map<String, Object> creditTest = new LinkedHashMap<>();
        creditTest.put("name", "Get User Credit");
        creditTest.put("endpoint", "GET /api/sms/credit/TEST001");
        creditTest.put("curl", "curl http://localhost:8087/api/sms/credit/TEST001");
        creditTest.put("expectedStatus", 200);
        tests.put("4_getCredit", creditTest);

        // HLR Lookup Test
        Map<String, Object> hlrTest = new LinkedHashMap<>();
        hlrTest.put("name", "HLR Lookup");
        hlrTest.put("endpoint", "GET /api/hlr/lookup/60123456789");
        hlrTest.put("curl", "curl http://localhost:8087/api/hlr/lookup/60123456789");
        hlrTest.put("expectedStatus", 200);
        tests.put("5_hlrLookup", hlrTest);

        // Network Operator Test
        Map<String, Object> networkTest = new LinkedHashMap<>();
        networkTest.put("name", "Get Network Operator");
        networkTest.put("endpoint", "GET /api/hlr/network/60123456789");
        networkTest.put("curl", "curl http://localhost:8087/api/hlr/network/60123456789");
        networkTest.put("expectedStatus", 200);
        tests.put("6_networkOperator", networkTest);

        // Celcom DN Test
        Map<String, Object> celcomDnTest = new LinkedHashMap<>();
        celcomDnTest.put("name", "Celcom DN");
        celcomDnTest.put("endpoint", "GET /dn/celcom");
        celcomDnTest.put("curl", "curl 'http://localhost:8087/dn/celcom?SMP_Txid=TX001&SUB_Mobtel=60123456789&" +
                "DNStatus=DELIVERED&ErrorCode=DeliveredToTerminal'");
        celcomDnTest.put("expectedStatus", 200);
        tests.put("7_celcomDn", celcomDnTest);

        // Digi DN Test
        Map<String, Object> digiDnTest = new LinkedHashMap<>();
        digiDnTest.put("name", "Digi DN");
        digiDnTest.put("endpoint", "POST /dn/digi");
        digiDnTest.put("curl", "curl -X POST 'http://localhost:8087/dn/digi?messageId=MSG001&status=DELIVERED&" +
                "msisdn=60163456789&errorCode=0'");
        digiDnTest.put("expectedStatus", 200);
        tests.put("8_digiDn", digiDnTest);

        response.put("tests", tests);

        return ResponseEntity.ok(response);
    }
}

