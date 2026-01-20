package com.isentric.smsserver.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * API Documentation Controller
 * Provides API documentation at /api-docs
 */
@RestController
public class ApiDocController {

    @GetMapping("/api-docs")
    public ResponseEntity<Map<String, Object>> getApiDocumentation() {
        Map<String, Object> docs = new LinkedHashMap<>();
        docs.put("openapi", "3.0.0");

        Map<String, Object> info = new LinkedHashMap<>();
        info.put("title", "ExtMTPush API");
        info.put("version", "1.0.0");
        info.put("description", "REST API for ExtMTPush SMS Gateway - Converted from legacy JAR to Spring Boot");
        docs.put("info", info);

        Map<String, Object> servers = new LinkedHashMap<>();
        servers.put("url", "http://localhost:8087");
        servers.put("description", "Development server");
        docs.put("servers", new Object[]{servers});

        Map<String, Object> paths = new LinkedHashMap<>();

        // SMS APIs
        paths.put("/api/sms/send", createEndpoint("POST", "Send SMS",
            "Send a single SMS message",
            "SMSMessageExtMTObject",
            "{\"shortcode\":\"12345\",\"custid\":\"TEST001\",\"rmsisdn\":\"60123456789\",\"smsisdn\":\"SENDER\",\"dataStr\":\"Hello World\"}"));

        paths.put("/api/sms/bulk/send", createEndpoint("POST", "Send Bulk SMS",
            "Send bulk SMS messages",
            "ExtSMSObject",
            "{\"billFlag\":\"1\",\"shortcode\":\"12345\",\"custid\":\"TEST001\",\"rmsisdn\":\"60123456789\",\"dataStr\":\"Bulk message\"}"));

        paths.put("/api/sms/status/{mtid}", createEndpoint("GET", "Check Delivery Status",
            "Check the delivery status of a sent message",
            null,
            null));

        paths.put("/api/sms/credit/{custid}", createEndpoint("GET", "Get User Credit",
            "Get credit balance for a customer",
            null,
            null));

        paths.put("/api/sms/credit/deduct", createEndpoint("POST", "Deduct Credit",
            "Deduct credit from customer account (params: custid, amount)",
            null,
            null));

        paths.put("/api/sms/health", createEndpoint("GET", "Health Check",
            "Check if the service is running",
            null,
            null));

        // HLR APIs
        paths.put("/api/hlr/lookup/{msisdn}", createEndpoint("GET", "HLR Lookup",
            "Perform HLR lookup for a single MSISDN",
            null,
            null));

        paths.put("/api/hlr/batch", createEndpoint("POST", "Batch HLR Lookup",
            "Perform HLR lookup for multiple MSISDNs",
            "String[]",
            "[\"60123456789\",\"60163456789\"]"));

        paths.put("/api/hlr/network/{msisdn}", createEndpoint("GET", "Get Network Operator",
            "Get the network operator for an MSISDN",
            null,
            null));

        paths.put("/api/hlr/validate/{msisdn}", createEndpoint("GET", "Validate MSISDN",
            "Validate if an MSISDN is valid",
            null,
            null));

        // DN APIs
        paths.put("/dn/celcom", createEndpoint("POST/GET", "Celcom DN",
            "Handle Celcom delivery notification (params: SMP_Txid, SUB_Mobtel, ErrorCode, DNStatus)",
            null,
            null));

        paths.put("/dn/digi", createEndpoint("POST", "Digi DN",
            "Handle Digi delivery notification (params: messageId, status, msisdn, errorCode)",
            null,
            null));

        paths.put("/dn/maxis", createEndpoint("POST", "Maxis DN",
            "Handle Maxis delivery notification (params: messageId, status, msisdn, errorCode)",
            null,
            null));

        paths.put("/dn/silverstreet", createEndpoint("POST", "SilverStreet DN",
            "Handle SilverStreet delivery notification (params: messageId, status, msisdn, errorCode)",
            null,
            null));

        paths.put("/dn/{operator}", createEndpoint("GET", "Generic DN",
            "Handle generic delivery notification for any operator",
            null,
            null));

        docs.put("paths", paths);

        // Schemas
        Map<String, Object> schemas = new LinkedHashMap<>();

        schemas.put("SMSMessageExtMTObject", createSchema(new String[][]{
            {"shortcode", "string", "Shortcode"},
            {"custid", "string", "Customer ID"},
            {"rmsisdn", "string", "Recipient MSISDN"},
            {"smsisdn", "string", "Sender ID"},
            {"mtid", "string", "Message ID"},
            {"price", "string", "Price code (e.g., 000)"},
            {"dataStr", "string", "Message content"},
            {"keyword", "string", "Keyword"},
            {"dataEncoding", "integer", "Data encoding (0=GSM7, 8=UCS2)"}
        }));

        schemas.put("ExtSMSObject", createSchema(new String[][]{
            {"billFlag", "string", "Bill flag (0=free, 1=billable)"},
            {"shortcode", "string", "Shortcode"},
            {"custid", "string", "Customer ID"},
            {"rmsisdn", "string", "Recipient MSISDN"},
            {"smsisdn", "string", "Sender ID"},
            {"mtid", "string", "Message ID"},
            {"mtprice", "string", "Message price code"},
            {"productType", "integer", "Product type (4=text, 10=WAP push)"},
            {"productCode", "string", "Product code"},
            {"dataEncoding", "integer", "Data encoding"},
            {"dataStr", "string", "Message content"},
            {"dataUrl", "string", "URL for WAP push"},
            {"dnrep", "integer", "Delivery report flag"}
        }));

        schemas.put("HLRResponse", createSchema(new String[][]{
            {"msisdn", "string", "MSISDN"},
            {"status", "string", "Lookup status (VALID/ERROR)"},
            {"networkCode", "string", "Network code"},
            {"countryCode", "string", "Country code"},
            {"ported", "string", "Ported status"},
            {"roaming", "string", "Roaming status"}
        }));

        Map<String, Object> components = new LinkedHashMap<>();
        components.put("schemas", schemas);
        docs.put("components", components);

        return ResponseEntity.ok(docs);
    }

    private Map<String, Object> createEndpoint(String method, String summary, String description, String requestBody, String example) {
        Map<String, Object> endpoint = new LinkedHashMap<>();
        endpoint.put("method", method);
        endpoint.put("summary", summary);
        endpoint.put("description", description);
        if (requestBody != null) {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("type", requestBody);
            body.put("example", example);
            endpoint.put("requestBody", body);
        }
        return endpoint;
    }

    private Map<String, Object> createSchema(String[][] properties) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");

        Map<String, Object> props = new LinkedHashMap<>();
        for (String[] prop : properties) {
            Map<String, String> propDef = new LinkedHashMap<>();
            propDef.put("type", prop[1]);
            propDef.put("description", prop[2]);
            props.put(prop[0], propDef);
        }
        schema.put("properties", props);

        return schema;
    }

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> index() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("application", "ExtMTPush");
        response.put("version", "1.0.0");
        response.put("status", "running");

        Map<String, String> links = new LinkedHashMap<>();
        links.put("health", "/api/sms/health");
        links.put("apiDocs", "/api-docs");
        links.put("testPage", "/test.html");
        links.put("apiTest", "/api/test");
        response.put("links", links);

        return ResponseEntity.ok(response);
    }
}

