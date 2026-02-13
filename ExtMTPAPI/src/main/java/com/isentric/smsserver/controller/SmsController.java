package com.isentric.smsserver.controller;

import com.isentric.smsserver.dto.SmsRequestDto;
import com.isentric.smsserver.dto.SmsResponseDto;
import com.isentric.smsserver.service.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/extmtpush")
@Slf4j
@RequiredArgsConstructor
public class SmsController {
    
    private final SmsService smsService;
    
    /**
     * Serve the HTML form for SMS submission
     */
    @GetMapping
    public String showForm(Model model) {
        return "extmtpush";
    }

    /**
     * Handle API GET requests with parameters
     */
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<String> handleSmsGet(
            @RequestParam(required = false) String shortcode,
            @RequestParam(required = false) String custid,
            @RequestParam(required = false) String rmsisdn,
            @RequestParam(required = false) String smsisdn,
            @RequestParam(required = false) String mtid,
            @RequestParam(required = false) String mtprice,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) Integer productType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer dataEncoding,
            @RequestParam(required = false) String dataStr,
            @RequestParam(required = false) String dataUrl,
            @RequestParam(required = false) Integer dnRep,
            @RequestParam(required = false) String groupTag,
            @RequestParam(required = false) String urlTitle,
            @RequestParam(required = false) String ewigFlag,
            @RequestParam(required = false, defaultValue = "0") String cFlag,
            HttpServletRequest request) {
        
        SmsRequestDto smsRequest = new SmsRequestDto(
            shortcode, custid, rmsisdn, smsisdn, mtid, mtprice,
            productCode, productType, keyword, dataEncoding, dataStr,
            dataUrl, dnRep, groupTag, urlTitle, ewigFlag, cFlag
        );
        
        return processRequest(smsRequest, request);
    }


    
    @PostMapping
    @ResponseBody
    public ResponseEntity<String> handleSmsPost(
            @RequestBody SmsRequestDto smsRequest,
            HttpServletRequest request) {
        
        return processRequest(smsRequest, request);
    }
    
    /**
     * Process SMS request and return response
     */
    private ResponseEntity<String> processRequest(SmsRequestDto smsRequest, HttpServletRequest request) {
        String ipAddress = getClientIpAddress(request);
        
        log.info("SMS Request received from IP: {}, Customer: {}, MTID: {}", 
                 ipAddress, smsRequest.getCustid(), smsRequest.getMtid());
        
        SmsResponseDto response = smsService.processSmsRequest(smsRequest, ipAddress);
        
        String responseText = formatResponse(response);
        log.info("SMS Response: returnCode={}, message={}", response.getReturnCode(), response.getReturnMsg());
        
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(responseText);
    }
    
    /**
     * Format response in legacy format
     */
    private String formatResponse(SmsResponseDto response) {
        return String.format(
            "MT Receive Result : returnCode = %d,messageID = %s,MSISDN = %s,returnMsg = %s\n ------------- ",
            response.getReturnCode(),
            response.getMessageId() != null ? response.getMessageId() : "",
            response.getMsisdn() != null ? response.getMsisdn() : "",
            response.getReturnMsg()
        );
    }
    
    /**
     * Get client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}

