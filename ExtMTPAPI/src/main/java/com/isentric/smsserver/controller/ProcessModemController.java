package com.isentric.smsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ProcessModem")
@Slf4j
@RequiredArgsConstructor
public class ProcessModemController {
    
    /**
     * Process modem messages (MO - Mobile Originated)
     */
    @PostMapping
    public ResponseEntity<String> processModem(
            @RequestParam(required = false) String msisdn,
            @RequestParam(required = false) String shortcode,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String timestamp) {
        
        log.info("Modem message received - MSISDN: {}, Shortcode: {}, Message: {}", 
                 msisdn, shortcode, message);
        
        // TODO: Process incoming MO message
        // This would typically:
        // 1. Parse the message
        // 2. Route to appropriate handler based on keyword
        // 3. Generate MT response if needed
        // 4. Log to database
        
        return ResponseEntity.ok("Message received and processed");
    }
}

