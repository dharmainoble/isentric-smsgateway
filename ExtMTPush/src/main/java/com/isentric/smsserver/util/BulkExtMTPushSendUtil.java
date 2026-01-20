package com.isentric.smsserver.util;

import com.isentric.smsserver.constant.ApplicationConstant;
import com.isentric.smsserver.object.ExtSMSObject;
import com.isentric.smsserver.object.SMSMessageExtMTObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Utility class for sending bulk MT Push messages
 * Based on original BulkExtMTPushSendUtil from ExtMTPush.jar
 */
@Component
public class BulkExtMTPushSendUtil {

    private static final Logger logger = LoggerFactory.getLogger(BulkExtMTPushSendUtil.class);

    // Product type constants from original code
    private static final int PRODUCT_TEXT = 4;
    private static final int PRODUCT_WAP_PUSH = 10;

    private final DBUtil dbUtil;
    private final SMSUtil smsUtil;
    private final ExecutorService executorService;

    @Autowired
    public BulkExtMTPushSendUtil(DBUtil dbUtil, SMSUtil smsUtil) {
        this.dbUtil = dbUtil;
        this.smsUtil = smsUtil;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    /**
     * Process pending messages from database (from original processPendingMessages)
     */
    public void processPendingMessages() {
        logger.info("Processing pending MT messages");

        try {
            String sql = "SELECT * FROM extmtpush_receive_bulk WHERE process_flag = 'P' ORDER BY received_date ASC LIMIT 100";
            List<Map<String, Object>> pendingMessages = dbUtil.getAvatarJdbcTemplate().queryForList(sql);

            for (Map<String, Object> row : pendingMessages) {
                try {
                    ExtSMSObject extSMS = mapRowToExtSMSObject(row);
                    processMessage(extSMS);
                } catch (Exception e) {
                    logger.error("Error processing message: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Error fetching pending messages: {}", e.getMessage());
        }
    }

    /**
     * Process single message (from original processMessage)
     */
    private void processMessage(ExtSMSObject extSMS) {
        logger.debug("Processing message - mtid: {}, rmsisdn: {}", extSMS.getMtid(), extSMS.getRmsisdn());

        try {
            // Determine telco
            String telco = SMSUtil.getTelco(extSMS.getRmsisdn());
            logger.debug("Telco for {}: {}", extSMS.getRmsisdn(), telco);

            // Route message based on telco
            String result = routeMessage(extSMS, telco);

            // Update status
            String newStatus = result.startsWith("SUCCESS") ? "S" : "F";
            updateMessageStatus(extSMS.getMtid(), newStatus, result);

        } catch (Exception e) {
            logger.error("Error processing message {}: {}", extSMS.getMtid(), e.getMessage());
            updateMessageStatus(extSMS.getMtid(), "F", e.getMessage());
        }
    }

    /**
     * Route message to appropriate gateway (from original routeMessage)
     */
    private String routeMessage(ExtSMSObject extSMS, String telco) {
        logger.debug("Routing message to telco: {}", telco);

        // Check if it's a WAP push or text message
        int productType = extSMS.getProductType();

        if (productType == PRODUCT_WAP_PUSH) {
            return sendWapPush(extSMS, telco);
        } else {
            return sendTextMessage(extSMS, telco);
        }
    }

    /**
     * Send text message (from original sendTextMessage)
     */
    private String sendTextMessage(ExtSMSObject extSMS, String telco) {
        logger.debug("Sending text message to: {}", extSMS.getRmsisdn());

        try {
            // Log to send table
            String insertSql = "INSERT INTO extmtpush_send_bulk " +
                    "(shortcode, custid, rmsisdn, smsisdn, mtid, mtprice, product_type, " +
                    "product_code, keyword, data_encoding, data_str, dnrep, group_tag, " +
                    "telco, send_date, status) VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), 'PENDING')";

            dbUtil.getAvatarJdbcTemplate().update(insertSql,
                    extSMS.getShortcode(),
                    extSMS.getCustid(),
                    extSMS.getRmsisdn(),
                    extSMS.getSmsisdn(),
                    extSMS.getMtid(),
                    extSMS.getMtprice(),
                    extSMS.getProductType(),
                    extSMS.getProductCode(),
                    extSMS.getKeyword(),
                    extSMS.getDataEncoding(),
                    extSMS.getDataStr(),
                    extSMS.getDnRep(),
                    extSMS.getGroupTag(),
                    telco);

            // Deduct credit if billable
            if ("1".equals(extSMS.getBillFlag())) {
                deductCredit(extSMS.getCustid(), 1);
            }

            return "SUCCESS: Message queued for " + extSMS.getRmsisdn();

        } catch (Exception e) {
            logger.error("Error sending text message: {}", e.getMessage());
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Send WAP push message (from original sendWapPush)
     */
    private String sendWapPush(ExtSMSObject extSMS, String telco) {
        logger.debug("Sending WAP push to: {}", extSMS.getRmsisdn());

        try {
            // Convert to hex for WAP push
            String hexData = SMSUtil.toHex(extSMS.getDataUrl(), extSMS.getUrlTitle());

            // Log to send table
            String insertSql = "INSERT INTO extmtpush_send_bulk " +
                    "(shortcode, custid, rmsisdn, smsisdn, mtid, mtprice, product_type, " +
                    "product_code, keyword, data_encoding, data_str, data_url, url_title, " +
                    "dnrep, group_tag, telco, send_date, status, hex_data) VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), 'PENDING', ?)";

            dbUtil.getAvatarJdbcTemplate().update(insertSql,
                    extSMS.getShortcode(),
                    extSMS.getCustid(),
                    extSMS.getRmsisdn(),
                    extSMS.getSmsisdn(),
                    extSMS.getMtid(),
                    extSMS.getMtprice(),
                    extSMS.getProductType(),
                    extSMS.getProductCode(),
                    extSMS.getKeyword(),
                    extSMS.getDataEncoding(),
                    extSMS.getDataStr(),
                    extSMS.getDataUrl(),
                    extSMS.getUrlTitle(),
                    extSMS.getDnRep(),
                    extSMS.getGroupTag(),
                    telco,
                    hexData);

            // Deduct credit if billable
            if ("1".equals(extSMS.getBillFlag())) {
                deductCredit(extSMS.getCustid(), 1);
            }

            return "SUCCESS: WAP push queued for " + extSMS.getRmsisdn();

        } catch (Exception e) {
            logger.error("Error sending WAP push: {}", e.getMessage());
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Update message status (from original updateMessageStatus)
     */
    private void updateMessageStatus(String mtid, String status, String remarks) {
        try {
            String sql = "UPDATE extmtpush_receive_bulk SET process_flag = ?, remarks = ?, " +
                    "processed_date = NOW() WHERE mtid = ?";
            dbUtil.getAvatarJdbcTemplate().update(sql, status, remarks, mtid);
        } catch (Exception e) {
            logger.error("Error updating message status: {}", e.getMessage());
        }
    }

    /**
     * Deduct credit (from original deductCredit)
     */
    private void deductCredit(String custid, int amount) {
        try {
            String sql = "UPDATE bulk_credit SET credit = credit - ? WHERE custid = ? AND credit >= ?";
            dbUtil.getAvatarJdbcTemplate().update(sql, amount, custid, amount);
        } catch (Exception e) {
            logger.error("Error deducting credit: {}", e.getMessage());
        }
    }

    /**
     * Map database row to ExtSMSObject
     */
    private ExtSMSObject mapRowToExtSMSObject(Map<String, Object> row) {
        ExtSMSObject extSMS = new ExtSMSObject();
        extSMS.setBillFlag(getString(row, "bill_flag"));
        extSMS.setProcessFlag(getString(row, "process_flag"));
        extSMS.setShortcode(getString(row, "shortcode"));
        extSMS.setCustid(getString(row, "custid"));
        extSMS.setRmsisdn(getString(row, "rmsisdn"));
        extSMS.setSmsisdn(getString(row, "smsisdn"));
        extSMS.setMtid(getString(row, "mtid"));
        extSMS.setMtprice(getString(row, "mtprice"));
        extSMS.setProductType(getInt(row, "product_type"));
        extSMS.setProductCode(getString(row, "product_code"));
        extSMS.setKeyword(getString(row, "keyword"));
        extSMS.setDataEncoding(getInt(row, "data_encoding"));
        extSMS.setDataStr(getString(row, "data_str"));
        extSMS.setDataUrl(getString(row, "data_url"));
        extSMS.setUrlTitle(getString(row, "url_title"));
        extSMS.setDnRep(getInt(row, "dnrep"));
        extSMS.setGroupTag(getString(row, "group_tag"));
        extSMS.setEwigFlag(getString(row, "ewig_flag"));
        return extSMS;
    }

    private String getString(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString() : "";
    }

    private int getInt(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    /**
     * Send bulk SMS messages
     */
    public List<String> sendBulkSMS(List<SMSMessageExtMTObject> messages) {
        logger.info("Sending bulk SMS - count: {}", messages.size());

        List<String> results = new ArrayList<>();
        List<CompletableFuture<String>> futures = new ArrayList<>();

        for (SMSMessageExtMTObject message : messages) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return sendSingleSMS(message);
            }, executorService);
            futures.add(future);
        }

        for (CompletableFuture<String> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                logger.error("Error getting bulk SMS result: {}", e.getMessage());
                results.add("ERROR: " + e.getMessage());
            }
        }

        return results;
    }

    /**
     * Send single SMS message
     */
    private String sendSingleSMS(SMSMessageExtMTObject message) {
        try {
            logger.debug("Sending SMS to: {}", message.getRmsisdn());

            // Validate MSISDN
            if (!SMSUtil.isValidMSISDN(message.getRmsisdn())) {
                return "ERROR: Invalid MSISDN - " + message.getRmsisdn();
            }

            // Format MSISDN
            String formattedMsisdn = SMSUtil.formatMSISDN(message.getRmsisdn());

            // Determine encoding
            int encoding = SMSUtil.getDataEncoding(message.getDataStr());
            message.setDataEncoding(encoding);

            // Create ExtSMSObject and process
            ExtSMSObject extSMS = new ExtSMSObject();
            extSMS.setShortcode(message.getShortcode());
            extSMS.setCustid(message.getCustid());
            extSMS.setRmsisdn(formattedMsisdn);
            extSMS.setSmsisdn(message.getSmsisdn());
            extSMS.setMtid(message.getMtid());
            extSMS.setMtprice(message.getMtprice());
            extSMS.setProductType(message.getProductType());
            extSMS.setProductCode(message.getProductCode());
            extSMS.setKeyword(message.getKeyword());
            extSMS.setDataEncoding(encoding);
            extSMS.setDataStr(message.getDataStr());
            extSMS.setDataUrl(message.getDataUrl());
            extSMS.setUrlTitle(message.getUrlTitle());
            extSMS.setDnRep(message.getDnrep());
            extSMS.setGroupTag(message.getGroupTag());
            extSMS.setBillFlag("1");

            String telco = SMSUtil.getTelco(formattedMsisdn);
            return routeMessage(extSMS, telco);

        } catch (Exception e) {
            logger.error("Error sending SMS to {}: {}", message.getRmsisdn(), e.getMessage());
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Send bulk SMS from ExtSMSObject list
     */
    public List<String> sendBulkSMSFromExtObjects(List<ExtSMSObject> extSmsObjects) {
        logger.info("Sending bulk SMS from ExtSMSObjects - count: {}", extSmsObjects.size());

        List<String> results = new ArrayList<>();
        for (ExtSMSObject extObj : extSmsObjects) {
            try {
                String telco = SMSUtil.getTelco(extObj.getRmsisdn());
                String result = routeMessage(extObj, telco);
                results.add(result);
            } catch (Exception e) {
                results.add("ERROR: " + e.getMessage());
            }
        }
        return results;
    }

    /**
     * Get bulk sending statistics
     */
    public Map<String, Object> getBulkStats(String custid) {
        try {
            String sql = "SELECT COUNT(*) as total, " +
                    "SUM(CASE WHEN process_flag = 'S' THEN 1 ELSE 0 END) as success, " +
                    "SUM(CASE WHEN process_flag = 'F' THEN 1 ELSE 0 END) as failed, " +
                    "SUM(CASE WHEN process_flag = 'P' THEN 1 ELSE 0 END) as pending " +
                    "FROM extmtpush_receive_bulk WHERE custid = ? AND DATE(received_date) = CURDATE()";
            return dbUtil.getAvatarJdbcTemplate().queryForMap(sql, custid);
        } catch (Exception e) {
            logger.error("Error getting bulk stats: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Shutdown executor service
     */
    public void shutdown() {
        executorService.shutdown();
    }
}
