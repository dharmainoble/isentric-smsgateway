package com.isentric.bulkgateway.controller;


import com.isentric.bulkgateway.dto.OperationError;
import com.isentric.bulkgateway.dto.SMSMessageDTO;
import com.isentric.bulkgateway.model.SMSMessageResponse;
import com.isentric.bulkgateway.service.SMSMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * SMS Message Controller
 * REST API for sending SMS messages
 * Replaces legacy SendMessageServlet
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
@CrossOrigin(origins = "*")
public class SMSMessageController {

    @Autowired(required = false)
    private SMSMessageService sMSMessageService;

    /**
     * Send a single SMS message
     */
    @PostMapping("/send")
    public ResponseEntity<SMSMessageResponse> sendMessage(@RequestBody SMSMessageDTO dto) {
        try {
            SMSMessageResponse response = sMSMessageService.sendMessage(dto);
                if (response.getStatus().equals("STATUS_SUCCESS")) {
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }
        } catch (Exception e) {
            log.error("Error sending message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error: " + e.getMessage()));
        }
    }

  /*  *//**
     * Send message via GET (legacy support)
     *//*
    @GetMapping("/send")
    public ResponseEntity<Map<String, Object>> sendMessageGet(
            @RequestParam String recipient,
            @RequestParam String message,
            @RequestParam(defaultValue = "maxis") String telco,
            @RequestParam(defaultValue = "smpp") String smsc) {

        try {
            SMSMessageResponse smsMsg = new SMSMessageResponse();
            smsMsg.setRecipient(recipient);
            smsMsg.setMessage(message);
            smsMsg.setTelco(telco);
            smsMsg.setSmsc(smsc);
            smsMsg.setMoid("0");
            smsMsg.setGroupId("api_request");
            smsMsg.setSender("API");
            smsMsg.setMessageType(0);

            if (bulkGatewayService != null) {
                SMSMessageResponse response = bulkGatewayService.sendTextMessage(smsMsg);
                return ResponseEntity.ok(mapResponse(response));
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(createErrorMap("Service not available"));
            }
        } catch (Exception e) {
            log.error("Error sending message via GET", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("Error: " + e.getMessage()));
        }
    }

    *//**
     * Check message status
     *//*
    @GetMapping("/{messageId}/status")
    public ResponseEntity<Map<String, Object>> checkStatus(@PathVariable String messageId) {
        try {
            // Implementation depends on your database structure
            Map<String, Object> response = new HashMap<>();
            response.put("messageId", messageId);
            response.put("status", "processing");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error checking status", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorMap("Error: " + e.getMessage()));
        }
    }

    private SMSMessageResponse createErrorResponse(String message) {
        SMSMessageResponse response = new SMSMessageResponse();
        response.setStatus(-1);

        // Create an error array with the error message
        OperationError error = new OperationError();
        error.setMessage(message);
        response.setErrorArray(new OperationError[]{error});

        return response;
    }

    private Map<String, Object> createErrorMap(String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", -1);
        map.put("statusMessage", message);
        return map;
    }

    private Map<String, Object> mapResponse(SMSMessageResponse response) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", response.getStatus());

        // Build status message from error array if errors exist
        String statusMessage = "Success";
        if (response.getStatus() != 0 && response.getErrorArray() != null && response.getErrorArray().length > 0) {
            statusMessage = response.getErrorArray()[0].getMessage();
        }

        map.put("statusMessage", statusMessage);
        map.put("messageId", response.getGuid());
        return map;
    }*/

    private SMSMessageResponse createErrorResponse(String message) {
        SMSMessageResponse response = new SMSMessageResponse();
        response.setStatus("Failed");

        // Create an error array with the error message
        OperationError error = new OperationError();
        error.setMessage(message);
        return response;
    }
}

