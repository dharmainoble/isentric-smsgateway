package com.isentric.smsserver.controller;

import com.isentric.smsserver.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling Delivery Notifications (DN) from various network operators
 */
@RestController
@RequestMapping("/dn")
public class DeliveryNotificationController {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryNotificationController.class);

    @Autowired
    private DBUtil dbUtil;

    /**
     * Handle Celcom Delivery Notification
     */
    @PostMapping("/celcom")
    public ResponseEntity<String> handleCelcomDN(
            @RequestParam(value = "SMP_Txid", required = false) String smpTxid,
            @RequestParam(value = "SUB_Mobtel", required = false) String subMobtel,
            @RequestParam(value = "APIType", required = false) String apiType,
            @RequestParam(value = "SMP_Keyword", required = false) String smpKeyword,
            @RequestParam(value = "SMP_ServiceID", required = false) String smpServiceId,
            @RequestParam(value = "DNStatus", required = false) String dnStatus,
            @RequestParam(value = "SMS_SourceAddr", required = false) String smsSourceAddr,
            @RequestParam(value = "ErrorCode", required = false) String errorCode) {

        logger.info("Received Celcom DN - SMP_Txid: {}, DNStatus: {}, ErrorCode: {}", smpTxid, dnStatus, errorCode);

        try {
            // Determine status based on ErrorCode (from original CelcomDNServlet logic)
            String status = "DeliveredToTerminal".equalsIgnoreCase(errorCode) ?
                    "STATE_DELIVERED" : "STATE_UNDELIVERABLE";

            // Process delivery notification
            processDN("HTTP_CELCOM", smpTxid, status, subMobtel, errorCode, dnStatus, smpServiceId);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logger.error("Error processing Celcom DN: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("ERROR");
        }
    }

    /**
     * Handle Celcom GET Delivery Notification
     */
    @GetMapping("/celcom")
    public ResponseEntity<String> handleCelcomDNGet(
            @RequestParam(value = "SMP_Txid", required = false) String smpTxid,
            @RequestParam(value = "SUB_Mobtel", required = false) String subMobtel,
            @RequestParam(value = "APIType", required = false) String apiType,
            @RequestParam(value = "SMP_Keyword", required = false) String smpKeyword,
            @RequestParam(value = "SMP_ServiceID", required = false) String smpServiceId,
            @RequestParam(value = "DNStatus", required = false) String dnStatus,
            @RequestParam(value = "SMS_SourceAddr", required = false) String smsSourceAddr,
            @RequestParam(value = "ErrorCode", required = false) String errorCode) {

        return handleCelcomDN(smpTxid, subMobtel, apiType, smpKeyword, smpServiceId, dnStatus, smsSourceAddr, errorCode);
    }

    /**
     * Handle Digi Delivery Notification
     */
    @PostMapping("/digi")
    public ResponseEntity<String> handleDigiDN(
            @RequestParam(value = "messageId", required = false) String messageId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "msisdn", required = false) String msisdn,
            @RequestParam(value = "deliveryTime", required = false) String deliveryTime,
            @RequestParam(value = "errorCode", required = false) String errorCode) {

        logger.info("Received Digi DN - messageId: {}, status: {}, msisdn: {}", messageId, status, msisdn);

        try {
            // Determine delivery status
            String smppStatus = mapStatusToSmppStatus(status, errorCode);

            // Process delivery notification
            processDN("HTTP_DIGI", messageId, smppStatus, msisdn, errorCode, status, null);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logger.error("Error processing Digi DN: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("ERROR");
        }
    }

    /**
     * Handle Maxis Delivery Notification
     */
    @PostMapping("/maxis")
    public ResponseEntity<String> handleMaxisDN(
            @RequestParam(value = "messageId", required = false) String messageId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "msisdn", required = false) String msisdn,
            @RequestParam(value = "deliveryTime", required = false) String deliveryTime,
            @RequestParam(value = "errorCode", required = false) String errorCode) {

        logger.info("Received Maxis DN - messageId: {}, status: {}, msisdn: {}", messageId, status, msisdn);

        try {
            // Determine delivery status
            String smppStatus = mapStatusToSmppStatus(status, errorCode);

            // Process delivery notification
            processDN("HTTP_MAXIS", messageId, smppStatus, msisdn, errorCode, status, null);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logger.error("Error processing Maxis DN: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("ERROR");
        }
    }

    /**
     * Handle SilverStreet Delivery Notification
     */
    @PostMapping("/silverstreet")
    public ResponseEntity<String> handleSilverStreetDN(
            @RequestParam(value = "messageId", required = false) String messageId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "msisdn", required = false) String msisdn,
            @RequestParam(value = "deliveryTime", required = false) String deliveryTime,
            @RequestParam(value = "errorCode", required = false) String errorCode) {

        logger.info("Received SilverStreet DN - messageId: {}, status: {}, msisdn: {}", messageId, status, msisdn);

        try {
            // Determine delivery status
            String smppStatus = mapStatusToSmppStatus(status, errorCode);

            // Process delivery notification
            processDN("HTTP_SILVERSTREET", messageId, smppStatus, msisdn, errorCode, status, null);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logger.error("Error processing SilverStreet DN: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("ERROR");
        }
    }

    /**
     * Generic GET endpoint for operators that use GET method
     */
    @GetMapping("/{operator}")
    public ResponseEntity<String> handleGenericDN(
            @PathVariable String operator,
            @RequestParam Map<String, String> params) {

        logger.info("Received {} DN via GET - params: {}", operator, params);

        try {
            String messageId = params.get("messageId");
            String status = params.get("status");
            String msisdn = params.get("msisdn");
            String errorCode = params.get("errorCode");

            String smppStatus = mapStatusToSmppStatus(status, errorCode);
            processDN("HTTP_" + operator.toUpperCase(), messageId, smppStatus, msisdn, errorCode, status, null);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            logger.error("Error processing {} DN: {}", operator, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("ERROR");
        }
    }

    /**
     * Map external status to SMPP status (based on original logic)
     */
    private String mapStatusToSmppStatus(String status, String errorCode) {
        if (status == null && errorCode == null) {
            return "STATE_UNKNOWN";
        }

        // Check for delivered status
        if ("DeliveredToTerminal".equalsIgnoreCase(errorCode) ||
            "DELIVERED".equalsIgnoreCase(status) ||
            "DELIVRD".equalsIgnoreCase(status) ||
            "1".equals(status)) {
            return "STATE_DELIVERED";
        }

        // Check for accepted/enroute status
        if ("ACCEPTD".equalsIgnoreCase(status) ||
            "ENROUTE".equalsIgnoreCase(status) ||
            "0".equals(status)) {
            return "STATE_ENROUTE";
        }

        // Check for expired status
        if ("EXPIRED".equalsIgnoreCase(status)) {
            return "STATE_EXPIRED";
        }

        // Check for rejected status
        if ("REJECTD".equalsIgnoreCase(status) || "REJECTED".equalsIgnoreCase(status)) {
            return "STATE_REJECTED";
        }

        // Default to undeliverable
        return "STATE_UNDELIVERABLE";
    }

    /**
     * Process Delivery Notification - Insert into database (based on original CelcomDNServlet logic)
     */
    private void processDN(String smppName, String messageId, String smppStatus,
                          String recipient, String errorCode, String dnStatus, String serviceId) {

        logger.debug("Processing DN - smppName: {}, messageId: {}, smppStatus: {}, recipient: {}, errorCode: {}, dnStatus: {}",
                smppName, messageId, smppStatus, recipient, errorCode, dnStatus);

        try {
            // Insert DN record into database (based on original CelcomDNServlet SQL)
            String sql = "INSERT INTO tbl_smpp_dn (smpServiceID, smppName, smppId, smppType, sender, recipient, " +
                        "timestamp, smppStatus, DNStatus, errorCode, dcs, date, bytes) VALUES " +
                        "(?, ?, ?, 'MT_STATUS', '', ?, NOW(), ?, ?, ?, '', NOW(), '')";

            int result = dbUtil.getAvatarJdbcTemplate().update(sql,
                    serviceId != null ? serviceId : "",
                    smppName,
                    messageId != null ? messageId : "",
                    recipient != null ? recipient : "",
                    smppStatus,
                    dnStatus != null ? dnStatus : "",
                    errorCode != null ? errorCode : "");

            logger.info("DN inserted successfully - messageId: {}, smppStatus: {}, result: {}",
                    messageId, smppStatus, result);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error inserting DN record: {}", e.getMessage(), e);
        }
    }
}

