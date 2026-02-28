package com.isentric.smsserver.service;

import com.isentric.smsserver.dto.SmsRequestDto;
import com.isentric.smsserver.dto.SmsResponseDto;
import com.isentric.smsserver.model.avatar.ExtMtId;
import com.isentric.smsserver.model.avatar.ExtMtPushReceive;
import com.isentric.smsserver.repository.avatar.ExtMtIdRepository;
import com.isentric.smsserver.repository.avatar.ExtMtPushReceiveRepository;
import com.isentric.smsserver.util.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SmsService {
    
    private final ExtMtPushReceiveRepository extMtPushReceiveRepository;
    private final ExtMtIdRepository extMtIdRepository;
    private final ValidationService validationService;
    private final CreditService creditService;
    private final ExternalApiService externalApiService;
    private final JmsTemplate jmsTemplate;
    
    public SmsService(
            ExtMtPushReceiveRepository extMtPushReceiveRepository,
            ExtMtIdRepository extMtIdRepository,
            ValidationService validationService,
            CreditService creditService,
            ExternalApiService externalApiService,
            @Autowired(required = false) JmsTemplate jmsTemplate) {
        this.extMtPushReceiveRepository = extMtPushReceiveRepository;
        this.extMtIdRepository = extMtIdRepository;
        this.validationService = validationService;
        this.creditService = creditService;
        this.externalApiService = externalApiService;
        this.jmsTemplate = jmsTemplate;
    }

    /**
     * Process incoming SMS push request
     */
    @Transactional("avatarTransactionManager")
    public SmsResponseDto processSmsRequest(SmsRequestDto request, String ipAddress) {
        log.info("Processing SMS request from IP: {}, Customer ID: {}, MTID: {}", 
                 ipAddress, request.getCustid(), request.getMtid());
        
        try {
            // Validate request parameters
            if (!validateParameters(request)) {
                return new SmsResponseDto(1, "Invalid parameters - ensure product type, data encoding and dnrep are integers");
            }
            
            // Validate package/IP
            if (!validationService.validatePackage(request.getShortcode(), request.getCustid(), ipAddress)) {
                return new SmsResponseDto(2, "Package validation failed - IP not authorized");
            }
            
            // Process multiple MSISDNs
            String[] msisdns = request.getRmsisdn().split(",");
            int msisdnCount = msisdns.length;
            
            for (int i = 0; i < msisdnCount; i++) {
                String currentMsisdn = msisdns[i].trim();
                String currentMtId = msisdnCount == 1 ? 
                                    request.getMtid() : 
                                    request.getMtid() + "_" + i;
                
                // Check for duplicate MTID
                if (extMtIdRepository.existsByMtidAndCustid(currentMtId, request.getCustid())) {
                    log.warn("Duplicate MTID: {} for customer: {}", currentMtId, request.getCustid());
                    return new SmsResponseDto(3, "Duplicate message ID");
                }
                
                // Normalize MSISDN
                currentMsisdn = SmsUtil.normalizeMsisdn(currentMsisdn);
                
                // Validate destination
                if (!validationService.validateDestination(request.getCustid(), currentMsisdn)) {
                    return new SmsResponseDto(4, "Destination validation failed");
                }
                
                // Check credit
                if (!creditService.hasCredit(request.getCustid())) {
                    return new SmsResponseDto(5, "Insufficient credit");
                }
                
                // Check blacklist/whitelist
                if (!validationService.checkBlackWhiteList(currentMtId, currentMsisdn, 
                        request.getShortcode(), request.getKeyword(), request.getCustid())) {
                    return new SmsResponseDto(6, "Blocked - blacklisted number");
                }
                
                // Check masking ID
                if (!validationService.checkMaskingId(request.getCustid(), request.getShortcode())) {
                    System.out.println(request.getCustid());
                    System.out.println(request.getShortcode());
                    return new SmsResponseDto(7, "Invalid masking ID");
                }
                
                // Insert MT record
                ExtMtPushReceive mtRecord = createMtRecord(request, currentMsisdn, currentMtId, "P");
                extMtPushReceiveRepository.save(mtRecord);
                
                // Insert MTID tracking
                ExtMtId mtId = new ExtMtId();
                mtId.setMtid(currentMtId);
                mtId.setCustid(request.getCustid());
                mtId.setDate(LocalDateTime.now());
                extMtIdRepository.save(mtId);

                // Send SMS to BulkGateway API
                boolean bulkGatewaySuccess = externalApiService.sendSmsToBulkGateway(mtRecord,currentMsisdn);
                if (!bulkGatewaySuccess) {
                    log.warn("Failed to send SMS to BulkGateway - MTID: {}, MSISDN: {}", currentMtId, currentMsisdn);
                }

                // ...existing code...
                if (jmsTemplate != null) {
                    jmsTemplate.convertAndSend("extmt.send.queue", mtRecord);
                    log.info("SMS queued successfully - MTID: {}, MSISDN: {}", currentMtId, currentMsisdn);
                } else {
                    log.warn("JMS is disabled - SMS saved to database but not queued - MTID: {}, MSISDN: {}", currentMtId, currentMsisdn);
                }
            }
            
            return new SmsResponseDto(0, request.getMtid(), request.getRmsisdn(), "Success");
            
        } catch (Exception e) {
            log.error("Error processing SMS request", e);
            return new SmsResponseDto(99, "Internal error: " + e.getMessage());
        }
    }
    
    /**
     * Validate request parameters
     */
    private boolean validateParameters(SmsRequestDto request) {
        return request.getProductType() != null && 
               request.getDataEncoding() != null && 
               request.getDnRep() != null &&
               request.getShortcode() != null && !request.getShortcode().isEmpty() &&
               request.getCustid() != null && !request.getCustid().isEmpty() &&
               request.getRmsisdn() != null && !request.getRmsisdn().isEmpty() &&
               request.getMtid() != null && !request.getMtid().isEmpty();
    }
    
    /**
     * Create MT record from request
     */
    private ExtMtPushReceive createMtRecord(SmsRequestDto request, String msisdn, 
                                            String mtid, String processFlag) {
        ExtMtPushReceive record = new ExtMtPushReceive();
        record.setBillFlag("1");
        record.setProcessFlag(processFlag);
        record.setShortcode(request.getShortcode());
        record.setCustid(request.getCustid());
        record.setRmsisdn(msisdn);
        record.setSmsisdn(request.getShortcode());
        record.setMtid(mtid);
        record.setMtprice(request.getMtprice());
        record.setProductType(request.getProductType());
        record.setProductCode(request.getProductCode());
        record.setKeyword(SmsUtil.removeSpecialChars(request.getKeyword()));
        record.setDataEncoding(request.getDataEncoding());
        record.setDataStr(SmsUtil.removeSpecialChars(request.getDataStr()));
        record.setDataUrl(request.getDataUrl());
        record.setUrlTitle(SmsUtil.removeSpecialChars(request.getUrlTitle()));
        record.setDnrep(request.getDnRep());
        record.setGroupTag(request.getGroupTag());
        record.setEwigFlag(request.getEwigFlag());
        record.setReceivedDate(LocalDateTime.now());
        return record;
    }
}

