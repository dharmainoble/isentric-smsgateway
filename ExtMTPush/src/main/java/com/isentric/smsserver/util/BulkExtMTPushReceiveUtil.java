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

/**
 * Utility class for processing bulk MT Push messages
 * Based on original BulkExtMTPushReceiveUtil from ExtMTPush.jar
 */
@Component
public class BulkExtMTPushReceiveUtil {

    private static final Logger logger = LoggerFactory.getLogger(BulkExtMTPushReceiveUtil.class);

    // Product type constants from original code
    private static final int PRODUCT_REMIX_RINGTONE = 0;
    private static final int PRODUCT_RINGTONE = 1;
    private static final int PRODUCT_LOGO = 2;
    private static final int PRODUCT_PICTURE = 3;
    private static final int PRODUCT_TEXT = 4;
    private static final int PRODUCT_WAP_POLYTONE = 5;
    private static final int PRODUCT_WAP_TRUETONE = 6;
    private static final int PRODUCT_WAP_PICTURE = 7;
    private static final int PRODUCT_WAP_JAVAGAME = 8;
    private static final int PRODUCT_WAP_THEME = 11;
    private static final int PRODUCT_WAP_EBOOK = 12;
    private static final int PRODUCT_WAP_ANIMATION = 13;
    private static final int PRODUCT_WAP_KARAOKE = 14;
    private static final int PRODUCT_WAP_VIDEO = 15;

    private static String returnMsg = "";
    private String contentDB = "";

    private final DBUtil dbUtil;

    @Autowired
    public BulkExtMTPushReceiveUtil(DBUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    public static String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String msg) {
        returnMsg = msg;
    }

    public String getContentDB() {
        return contentDB;
    }

    public void setContentDB(String db) {
        contentDB = db;
    }

    /**
     * Validate destination (from original validateDest)
     */
    public boolean validateDest(String custid, String rmsisdn) {
        if ((rmsisdn.length() == 10 || rmsisdn.length() == 11) && rmsisdn.startsWith("01")) {
            rmsisdn = "6" + rmsisdn;
        }

        try {
            String sql;
            String flagColumn;
            if (rmsisdn.startsWith("601")) {
                sql = "SELECT local_flag FROM bulk_destination_sms WHERE custid = ? AND local_flag = '0'";
                flagColumn = "local_flag";
            } else {
                sql = "SELECT int_flag FROM bulk_destination_sms WHERE custid = ? AND int_flag = '0'";
                flagColumn = "int_flag";
            }

            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);
            if (!results.isEmpty()) {
                setContentDB((String) results.get(0).get(flagColumn));
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error validating destination: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Validate product code (from original validateProductCode)
     */
    public boolean validateProductCode(String shortcode, String custid, String ipaddr,
                                       int productType, String productCode, String rmsisdn) {
        try {
            String contentTable = getContentTable(productType, rmsisdn);
            String sql = "SELECT code FROM " + getContentDB() + "." + contentTable +
                    " WHERE code = ?";

            if (productType == PRODUCT_REMIX_RINGTONE) {
                sql += " AND genre = 'Remix'";
            }

            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, productCode);
            return !results.isEmpty();
        } catch (Exception e) {
            logger.error("Error validating product code: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get content table based on product type (from original getContentTable)
     */
    public String getContentTable(int productType, String rmsisdn) {
        String telco = SMSUtil.getTelco(rmsisdn);

        switch (productType) {
            case PRODUCT_REMIX_RINGTONE:
            case PRODUCT_RINGTONE:
                return "sms_ringtones";
            case PRODUCT_LOGO:
                if ("6013".equals(telco)) return "sms_logos_tm";
                if ("6016".equals(telco) || "60146".equals(telco)) return "sms_logos_digi";
                if ("6019".equals(telco)) return "sms_logos_celcom";
                return "sms_logos";
            case PRODUCT_PICTURE:
                return "sms_pictures";
            case PRODUCT_WAP_POLYTONE:
                return "sms_wap_ringtones";
            case PRODUCT_WAP_TRUETONE:
                return "sms_wap_truetones";
            case PRODUCT_WAP_PICTURE:
                return "sms_wap_pictures";
            case PRODUCT_WAP_JAVAGAME:
                return "sms_wap_games";
            case PRODUCT_WAP_THEME:
                return "sms_wap_themes";
            case PRODUCT_WAP_EBOOK:
                return "sms_wap_ebooks";
            case PRODUCT_WAP_ANIMATION:
                return "sms_wap_animations";
            case PRODUCT_WAP_KARAOKE:
                return "sms_wap_karaokes";
            case PRODUCT_WAP_VIDEO:
                return "sms_wap_videos";
            default:
                return "sms_content";
        }
    }

    /**
     * Check blacklist/whitelist (from original checkBlackWhiteList)
     */
    public boolean checkBlackWhiteList(String mtid, String rmsisdn, String shortcode,
                                       String keyword, String custid) {
        try {
            // Check whitelist first
            String whitelistSql = "SELECT message_id FROM sms_generic_forwardmo_details " +
                    "WHERE message_id = ? AND RIGHT(msisdn, 9) = RIGHT(?, 9)";
            List<Map<String, Object>> whitelistResults = dbUtil.getAvatarJdbcTemplate()
                    .queryForList(whitelistSql, mtid, rmsisdn);

            if (!whitelistResults.isEmpty()) {
                return true; // Whitelisted
            }

            // Check blacklist
            String blacklistSql = "SELECT message_id FROM sms_generic_forwardmo_blacklist " +
                    "WHERE RIGHT(msisdn, 9) = RIGHT(?, 9) " +
                    "AND (blacklist_keyword LIKE ? OR blacklist_keyword LIKE 'all' OR blacklist_keyword LIKE 'semua') " +
                    "AND blacklist_flag = 0";
            List<Map<String, Object>> blacklistResults = dbUtil.getAvatarJdbcTemplate()
                    .queryForList(blacklistSql, rmsisdn, keyword);

            return blacklistResults.isEmpty(); // Not blacklisted
        } catch (Exception e) {
            logger.error("Error checking blacklist: {}", e.getMessage());
            return true; // Default allow
        }
    }

    /**
     * Check credit (from original checkCredit)
     */
    public boolean checkCredit(String custid) {
        try {
            String sql = "SELECT credit_type FROM bulk_credit WHERE custid = ? " +
                    "AND ((credit_type = -1) OR (credit_type = 1 AND credit > 1)) AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);
            return !results.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking credit: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Check masking ID (from original checkMaskingID)
     */
    public boolean checkMaskingID(String custid, String smsisdn) {
        try {
            String sql = "SELECT masking_id FROM bulk_masking_id WHERE custid = ? AND masking_id = ? AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid, smsisdn);
            return !results.isEmpty() || smsisdn.matches("^[0-9]+$");
        } catch (Exception e) {
            logger.error("Error checking masking ID: {}", e.getMessage());
            return true;
        }
    }

    /**
     * Insert MT detail (from original insertMTDetail)
     */
    public int insertMTDetail(String billFlag, String shortcode, String custid, String rmsisdn,
                              String smsisdn, String mtid, String mtprice, int productType,
                              String productCode, String keyword, int dataEncoding, String dataStr,
                              String dataUrl, String urlTitle, int dnrep, String groupTag,
                              String ewigFlag, String cFlag) {

        String processFlag = "P";

        // Check validations
        try {
            if (!checkBlackWhiteList(mtid, rmsisdn, shortcode, keyword, custid)) {
                processFlag = "B";
            } else if (!checkCredit(custid)) {
                processFlag = "C";
            } else if (!checkMaskingID(custid, smsisdn)) {
                processFlag = "M";
            }
        } catch (Exception e) {
            logger.error("Validation error: {}", e.getMessage());
        }

        // Check for duplicate
        try {
            String checkSql = "SELECT row_id FROM extmt_mtid WHERE mtid = ? AND custid = ?";
            List<Map<String, Object>> existing = dbUtil.getGeneralJdbcTemplate().queryForList(checkSql, mtid, custid);
            if (!existing.isEmpty()) {
                logger.debug("Incoming MT rejected - 3 - duplicate message id.");
                setReturnMsg("Incoming MT rejected - 3 - duplicate message id.\n");
                return 3;
            }
        } catch (Exception e) {
            logger.error("Error checking duplicate: {}", e.getMessage());
        }

        // Insert record
        try {
            String insertQuery = "INSERT INTO extmtpush_receive_bulk " +
                    "(bill_flag, process_flag, shortcode, custid, rmsisdn, smsisdn, mtid, mtprice, " +
                    "product_type, product_code, keyword, data_encoding, data_str, data_url, url_title, " +
                    "dnrep, group_tag, ewig_flag, received_date) VALUES " +
                    "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";

            dbUtil.getAvatarJdbcTemplate().update(insertQuery,
                    billFlag, processFlag, shortcode, custid, rmsisdn, smsisdn, mtid, mtprice,
                    productType, productCode, SMSUtil.removeSpecialChars(keyword), dataEncoding,
                    SMSUtil.removeSpecialChars(dataStr), dataUrl, SMSUtil.removeSpecialChars(urlTitle),
                    dnrep, groupTag, ewigFlag);

            // Insert message ID tracking
            String insertMtidQuery = "INSERT INTO extmt_mtid (mtid, custid, date) VALUES (?, ?, NOW())";
            dbUtil.getGeneralJdbcTemplate().update(insertMtidQuery, mtid, custid);

        } catch (Exception e) {
            logger.error("Error inserting record: {}", e.getMessage());
            return 1;
        }

        // Return based on process flag
        if ("B".equals(processFlag)) {
            logger.debug("Incoming MT rejected - 10 - MSISDN Blacklisted.");
            setReturnMsg("Incoming MT rejected - 10 - MSISDN Blacklisted.\n");
            return 10;
        }
        if ("C".equals(processFlag)) {
            logger.debug("Incoming MT rejected - 11 - Credit Error.");
            setReturnMsg("Incoming MT rejected - 11 - Credit Error. Make sure you have enough credit\n");
            return 11;
        }
        if ("M".equals(processFlag)) {
            logger.debug("Incoming MT rejected - 12 - Masking ID Error.");
            setReturnMsg("Incoming MT rejected - 12 - Masking ID Error. Please check the masking id\n");
            return 12;
        }

        logger.debug("Incoming MT accepted - 0 - Success.");
        setReturnMsg("Incoming MT accepted - 0 - Success.\n");
        return 0;
    }

    /**
     * Convert hex string to bytes (from original HextoByte)
     */
    public static byte[] hexToByte(String s) {
        int stringLength = s.length();
        if ((stringLength & 1) != 0) {
            throw new IllegalArgumentException("Hex string requires even number of characters");
        }
        byte[] b = new byte[stringLength / 2];
        for (int i = 0, j = 0; i < stringLength; i += 2, j++) {
            int high = charToNibble(s.charAt(i));
            int low = charToNibble(s.charAt(i + 1));
            b[j] = (byte) ((high << 4) | low);
        }
        return b;
    }

    private static int charToNibble(char c) {
        if ('0' <= c && c <= '9') return c - '0';
        if ('a' <= c && c <= 'f') return c - 'a' + 10;
        if ('A' <= c && c <= 'F') return c - 'A' + 10;
        throw new IllegalArgumentException("Invalid hex character: " + c);
    }

    /**
     * Process incoming MO message
     */
    public void processIncomingMessage(SMSMessageExtMTObject message) {
        logger.info("Processing incoming message from: {}", message.getSmsisdn());

        try {
            if (message.getSmsisdn() == null || message.getSmsisdn().isEmpty()) {
                logger.error("Invalid source MSISDN");
                return;
            }

            logger.debug("Incoming - shortcode: {}, msisdn: {}, message: {}",
                    message.getShortcode(), message.getSmsisdn(), message.getDataStr());

        } catch (Exception e) {
            logger.error("Error processing incoming message: {}", e.getMessage(), e);
        }
    }

    /**
     * Process batch of incoming messages
     */
    public void processBatchIncomingMessages(List<SMSMessageExtMTObject> messages) {
        logger.info("Processing batch of {} incoming messages", messages.size());
        for (SMSMessageExtMTObject message : messages) {
            processIncomingMessage(message);
        }
    }

    /**
     * Get pending incoming messages
     */
    public List<SMSMessageExtMTObject> getPendingMessages() {
        logger.info("Getting pending incoming messages");
        return new ArrayList<>();
    }

    /**
     * Mark message as processed
     */
    public void markAsProcessed(String messageId) {
        logger.info("Marking message as processed: {}", messageId);
        try {
            String sql = "UPDATE extmtpush_receive_bulk SET process_flag = 'D' WHERE mtid = ?";
            dbUtil.getAvatarJdbcTemplate().update(sql, messageId);
        } catch (Exception e) {
            logger.error("Error marking message as processed: {}", e.getMessage(), e);
        }
    }
}
