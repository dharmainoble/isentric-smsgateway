package com.isentric.bulkgateway.controller;


import com.isentric.bulkgateway.bg.model.SMSMessageResponse;
import com.isentric.bulkgateway.dto.OperationError;
import com.isentric.bulkgateway.dto.SMSMessageDTO;
import com.isentric.bulkgateway.service.SMSMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.nio.charset.StandardCharsets;

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
    public ResponseEntity<Object> sendMessage(@RequestBody SMSMessageDTO dto) {
        try {
            SMSMessageResponse response = sMSMessageService.sendMessage(dto);
            String status = response == null ? null : response.getStatus();
            if ("STATUS_SUCCESS".equals(status)) {
                return ResponseEntity.ok(response);
            } else {
                // Build a small human-readable error payload instead of returning raw byte[] fields
                Map<String,Object> body = new HashMap<>();
                body.put("statusCode", status == null || status.isEmpty() ? "STATUS_FAILURE" : status);
                // try to decode textual message bytes if present
                String statusMessage = null;
                if (response != null) {
                    byte[] msgBytes = response.getMessage();
                    if (msgBytes != null && msgBytes.length > 0) {
                        statusMessage = new String(msgBytes, StandardCharsets.UTF_8);
                    } else if (response.getValue() != null && response.getValue().length > 0) {
                        // value may contain serialized JSON; attempt to decode to UTF-8 for quick introspection
                        statusMessage = new String(response.getValue(), StandardCharsets.UTF_8);
                    }
                    body.put("guid", response.getGuid());
                } else {
                    statusMessage = "Unknown failure";
                }
                body.put("statusMessage", statusMessage == null ? "Failure" : statusMessage);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
            }
        } catch (Exception e) {
            log.error("Error sending message", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error: " + e.getMessage()));
        }
    }

    // Legacy/optional endpoints removed for clarity. See older branches if needed.

    private SMSMessageResponse createErrorResponse(String message) {
        SMSMessageResponse response = new SMSMessageResponse();
        response.setStatus("Failed");

        // Create an error array with the error message
        OperationError error = new OperationError();
        error.setMessage(message);
        return response;
    }
}
