package com.isentric.smsserver.controller;

import com.isentric.smsserver.dto.HlrLookupRequestDto;
import com.isentric.smsserver.service.HlrService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/HLRLookup")
@Slf4j
@RequiredArgsConstructor
public class HlrController {
    
    private final HlrService hlrService;
    
    @GetMapping
    public ResponseEntity<String> handleHlrLookup(
            @RequestParam(required = false) String shortcode,
            @RequestParam(required = false) String custid,
            @RequestParam(required = false) String msisdn,
            @RequestParam(required = false) String requestId) {
        
        log.info("HLR Lookup request - Customer: {}, MSISDN: {}, RequestID: {}", 
                 custid, msisdn, requestId);
        
        HlrService.HlrResponse hlrResponse = hlrService.performHlrLookup(msisdn, custid, requestId);
        
        String response = String.format(
            "HLR Lookup Result: requestId=%s,msisdn=%s,status=%s,carrier=%s,message=%s\n",
            hlrResponse.requestId(), 
            hlrResponse.msisdn(), 
            hlrResponse.status(),
            hlrResponse.carrier(),
            hlrResponse.message()
        );
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(response);
    }
}

