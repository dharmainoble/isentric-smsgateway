package com.isentric.smsserver.service;

import com.isentric.smsserver.object.ExtSMSObject;
import com.isentric.smsserver.object.SMSMessageExtMTObject;
import com.isentric.smsserver.util.DBUtil;
import com.isentric.smsserver.util.SMSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ExtMTPushService {

    private static final Logger logger = LoggerFactory.getLogger(ExtMTPushService.class);

    // Return codes from original BulkExtMTPushReceiveUtil
    private static final int SUCCESS = 0;
    private static final int ERROR_GENERAL = 1;
    private static final int ERROR_DUPLICATE_MTID = 3;
    private static final int ERROR_INVALID_MSISDN = 4;
    private static final int ERROR_INVALID_PRICE = 5;
    private static final int ERROR_INVALID_EWIG_FLAG = 6;
    private static final int ERROR_INVALID_URL_TITLE = 7;
    private static final int ERROR_INVALID_MESSAGE_LENGTH = 8;
    private static final int ERROR_BLACKLISTED = 10;
    private static final int ERROR_CREDIT = 11;
    private static final int ERROR_MASKING_ID = 12;
    private static final int ERROR_DESTINATION_BLOCKED = 13;

    private static String returnMsg = "";

    private final DBUtil dbUtil;
    private final SMSUtil smsUtil;

    @Autowired
    public ExtMTPushService(DBUtil dbUtil, SMSUtil smsUtil) {
        this.dbUtil = dbUtil;
        this.smsUtil = smsUtil;
    }

    public static String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String msg) {
        returnMsg = msg;
    }

    /**
     * Receive and process ExtMTPush message (based on original ExternalMTPushInterfaceSoapBindingImpl)
     */
    public int receiveExtMTPush(String shortcode, String custid, String rmsisdn, String smsisdn,
                                String messageid, String messageprice, int productType, String productCode,
                                String keyword, int dataEncoding, String dataStr, String dataUrl,
                                int dnrep, String groupTag, String remoteAdd, String urlTitle, String ewigFlag) {

        // Trim all parameters
        shortcode = trimSafe(shortcode);
        custid = trimSafe(custid);
        rmsisdn = trimSafe(rmsisdn);
        smsisdn = trimSafe(smsisdn);
        messageid = trimSafe(messageid);
        messageprice = trimSafe(messageprice);
        productCode = trimSafe(productCode);
        keyword = trimSafe(keyword);
        dataUrl = trimSafe(dataUrl);
        groupTag = trimSafe(groupTag);
        remoteAdd = trimSafe(remoteAdd);
        urlTitle = trimSafe(urlTitle);
        ewigFlag = trimSafe(ewigFlag);

        // Format MSISDN (from original logic)
        rmsisdn = formatMsisdn(rmsisdn);

        // Default values
        if (urlTitle == null || "null".equalsIgnoreCase(urlTitle)) urlTitle = "";
        if (ewigFlag == null || "null".equalsIgnoreCase(ewigFlag)) ewigFlag = "0";
        if (smsisdn == null || "null".equalsIgnoreCase(smsisdn) || smsisdn.isEmpty()) smsisdn = custid;

        logger.debug("Shortcode = {}", shortcode);
        logger.debug("Customer ID = {}", custid);
        logger.debug("Receiver MSISDN = {}", rmsisdn);
        logger.debug("Sender MSISDN = {}", smsisdn);
        logger.debug("Message ID = {}", messageid);
        logger.debug("Message Price = {}", messageprice);
        logger.debug("Product Type = {}", productType);
        logger.debug("Product Code = {}", productCode);
        logger.debug("Keyword = {}", keyword);
        logger.debug("Data Encoding = {}", dataEncoding);
        logger.debug("Data Url = {}", dataUrl);
        logger.debug("Report DN = {}", dnrep);
        logger.debug("Group Tag = {}", groupTag);
        logger.debug("URL Title = {}", urlTitle);
        logger.debug("Ewig Flag = {}", ewigFlag);
        logger.debug("Remote IP: {}", remoteAdd);
        logger.debug("Message: {} {}", dataStr, dataUrl);

        // Validation checks (from original logic)
        if (!validMSISDN(rmsisdn)) {
            logger.debug("Incoming MT rejected - 4 - check MSISDN.");
            setReturnMsg("Incoming MT rejected - 4 - check MSISDN.\n");
            return ERROR_INVALID_MSISDN;
        }

        if (!validateDest(custid, rmsisdn)) {
            logger.debug("Incoming MT rejected - 13 - Destination Blocked.");
            setReturnMsg("Incoming MT rejected - 13 - Destination Blocked.\n");
            return ERROR_DESTINATION_BLOCKED;
        }

        if (!validParam(messageid) || !validParam(messageprice)) {
            logger.debug("Incoming MT rejected - 1 - check message id and message price.");
            setReturnMsg("Incoming MT rejected - 1 - check message id and message price.\n");
            return ERROR_GENERAL;
        }

        if (!messageprice.matches("^[0-9]{3,4}$")) {
            logger.debug("Incoming MT rejected - 5 - check price code.");
            setReturnMsg("Incoming MT rejected - 5 - check price code.\n");
            return ERROR_INVALID_PRICE;
        }

        if (!checkDataStr(dataStr, productType)) {
            logger.debug("Incoming MT rejected - 8 - check your message length.");
            setReturnMsg("Incoming MT rejected - 8 - check your message length.\n");
            return ERROR_INVALID_MESSAGE_LENGTH;
        }

        // Insert MT detail
        try {
            return insertMTDetail("000".equals(messageprice) ? "0" : "1", shortcode, custid, rmsisdn, smsisdn,
                    messageid, messageprice, productType, productCode, keyword, dataEncoding,
                    dataStr, dataUrl, urlTitle, dnrep, groupTag, ewigFlag, "0");
        } catch (Exception e) {
            logger.error("Error inserting MT detail: {}", e.getMessage(), e);
            return ERROR_GENERAL;
        }
    }

    /**
     * Insert MT Detail into database (from original BulkExtMTPushReceiveUtil)
     */
    private int insertMTDetail(String billFlag, String shortcode, String custid, String rmsisdn,
                               String smsisdn, String mtid, String mtprice, int productType,
                               String productCode, String keyword, int dataEncoding, String dataStr,
                               String dataUrl, String urlTitle, int dnrep, String groupTag,
                               String ewigFlag, String cFlag) {

        // Check for blacklist, credit, masking ID
        String processFlag = "P"; // Default to process

        try {
            // Check blacklist
            if (!checkBlackWhiteList(mtid, rmsisdn, shortcode, keyword, custid)) {
                processFlag = "B";
            } else if (!checkCredit(custid)) {
                processFlag = "C";
            } else if (!checkMaskingID(custid, smsisdn)) {
                processFlag = "M";
            }
        } catch (Exception e) {
            logger.error("Error checking validation: {}", e.getMessage());
        }

        // Check for duplicate message ID
        if (isDuplicateMessageId(mtid, custid)) {
            logger.debug("Incoming MT rejected - 3 - duplicate message id.");
            setReturnMsg("Incoming MT rejected - 3 - duplicate message id.\n");
            return ERROR_DUPLICATE_MTID;
        }

        // Insert into receive table
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
            logger.error("Error inserting MT record: {}", e.getMessage(), e);
            return ERROR_GENERAL;
        }

        // Handle process flags
        if ("B".equalsIgnoreCase(processFlag)) {
            logger.debug("Incoming MT rejected - 10 - MSISDN Blacklisted.");
            setReturnMsg("Incoming MT rejected - 10 - MSISDN Blacklisted.\n");
            return ERROR_BLACKLISTED;
        }

        if ("C".equalsIgnoreCase(processFlag)) {
            logger.debug("Incoming MT rejected - 11 - Credit Error.");
            setReturnMsg("Incoming MT rejected - 11 - Credit Error. Make sure you have enough credit\n");
            return ERROR_CREDIT;
        }

        if ("M".equalsIgnoreCase(processFlag)) {
            logger.debug("Incoming MT rejected - 12 - Masking ID Error.");
            setReturnMsg("Incoming MT rejected - 12 - Masking ID Error. Please check the masking id\n");
            return ERROR_MASKING_ID;
        }

        logger.debug("Incoming MT accepted - 0 - Success.");
        setReturnMsg("Incoming MT accepted - 0 - Success.\n");
        return SUCCESS;
    }

    /**
     * Send SMS message
     */
    public String sendSMS(SMSMessageExtMTObject smsMessage) {
        logger.info("Sending SMS to: {}", smsMessage.getRmsisdn());

        try {
            // Validate MSISDN
            if (!validMSISDN(smsMessage.getRmsisdn())) {
                logger.error("Invalid MSISDN: {}", smsMessage.getRmsisdn());
                return "ERROR: Invalid MSISDN";
            }

            // Generate message ID if not provided
            String messageId = smsMessage.getMtid();
            if (messageId == null || messageId.isEmpty()) {
                messageId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
                smsMessage.setMtid(messageId);
            }

            // Process the message
            String formattedMsisdn = formatMsisdn(smsMessage.getRmsisdn());
            int dataEncoding = SMSUtil.getDataEncoding(smsMessage.getDataStr());
            smsMessage.setDataEncoding(dataEncoding);

            // Call receiveExtMTPush
            int result = receiveExtMTPush(
                    smsMessage.getShortcode(),
                    smsMessage.getCustid(),
                    formattedMsisdn,
                    smsMessage.getSmsisdn(),
                    messageId,
                    smsMessage.getPrice() != null ? smsMessage.getPrice() : "000",
                    0, // productType
                    "", // productCode
                    smsMessage.getKeyword() != null ? smsMessage.getKeyword() : "",
                    dataEncoding,
                    smsMessage.getDataStr(),
                    "", // dataUrl
                    1, // dnrep
                    "", // groupTag
                    "", // remoteAdd
                    "", // urlTitle
                    "0" // ewigFlag
            );

            if (result == SUCCESS) {
                return "SUCCESS: " + messageId;
            } else {
                return "ERROR: " + getReturnMsg();
            }
        } catch (Exception e) {
            logger.error("Error sending SMS: {}", e.getMessage(), e);
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Send bulk SMS
     */
    public String sendBulkSMS(ExtSMSObject extSmsObject) {
        logger.info("Sending bulk SMS to: {}", extSmsObject.getRmsisdn());

        try {
            if (!validMSISDN(extSmsObject.getRmsisdn())) {
                logger.error("Invalid MSISDN: {}", extSmsObject.getRmsisdn());
                return "ERROR: Invalid MSISDN";
            }

            // Generate message ID
            String messageId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

            int result = receiveExtMTPush(
                    extSmsObject.getShortcode(),
                    extSmsObject.getCustid(),
                    formatMsisdn(extSmsObject.getRmsisdn()),
                    extSmsObject.getSmsisdn(),
                    messageId,
                    extSmsObject.getMtprice() != null ? extSmsObject.getMtprice() : "000",
                    extSmsObject.getProductType(),
                    extSmsObject.getProductCode() != null ? extSmsObject.getProductCode() : "",
                    extSmsObject.getKeyword() != null ? extSmsObject.getKeyword() : "",
                    extSmsObject.getDataEncoding(),
                    extSmsObject.getDataStr(),
                    extSmsObject.getDataUrl() != null ? extSmsObject.getDataUrl() : "",
                    extSmsObject.getDnRep(),
                    extSmsObject.getGroupTag() != null ? extSmsObject.getGroupTag() : "",
                    "",
                    "",
                    "0"
            );

            if (result == SUCCESS) {
                return "SUCCESS: " + messageId;
            } else {
                return "ERROR: " + getReturnMsg();
            }
        } catch (Exception e) {
            logger.error("Error sending bulk SMS: {}", e.getMessage(), e);
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Check SMS delivery status
     */
    public String checkDeliveryStatus(String mtid) {
        logger.info("Checking delivery status for mtid: {}", mtid);

        try {
            String sql = "SELECT smppStatus FROM tbl_smpp_dn WHERE smppId = ? ORDER BY date DESC LIMIT 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, mtid);

            if (!results.isEmpty()) {
                return (String) results.get(0).get("smppStatus");
            }
            return "PENDING";
        } catch (Exception e) {
            logger.error("Error checking delivery status: {}", e.getMessage(), e);
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Get user credit (from original checkCredit logic)
     */
    public double getUserCredit(String custid) {
        logger.info("Getting credit for customer: {}", custid);

        try {
            String sql = "SELECT credit FROM bulk_credit WHERE custid = ? AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);

            if (!results.isEmpty()) {
                Object credit = results.get(0).get("credit");
                if (credit != null) {
                    return Double.parseDouble(credit.toString());
                }
            }
            return 0.0;
        } catch (Exception e) {
            logger.error("Error getting user credit: {}", e.getMessage(), e);
            return 0.0;
        }
    }

    /**
     * Deduct user credit
     */
    public boolean deductCredit(String custid, double amount) {
        logger.info("Deducting credit for customer: {}, amount: {}", custid, amount);

        try {
            String sql = "UPDATE bulk_credit SET credit = credit - ? WHERE custid = ? AND credit >= ? AND status = 1";
            int result = dbUtil.getAvatarJdbcTemplate().update(sql, amount, custid, amount);
            return result > 0;
        } catch (Exception e) {
            logger.error("Error deducting credit: {}", e.getMessage(), e);
            return false;
        }
    }

    // Helper methods from original code

    private String trimSafe(String s) {
        return s != null ? s.trim() : "";
    }

    private boolean validParam(String s) {
        return s != null && !s.isEmpty();
    }

    private boolean validMSISDN(String msisdn) {
        if (msisdn == null || msisdn.isEmpty()) return false;
        return msisdn.matches("^[0-9]{10,15}$");
    }

    private String formatMsisdn(String rmsisdn) {
        if (rmsisdn == null) return "";
        rmsisdn = rmsisdn.replace("-", "").replace("+", "");

        if ((rmsisdn.length() == 10 || rmsisdn.length() == 11) && rmsisdn.startsWith("01")) {
            rmsisdn = "6" + rmsisdn;
        }
        if (rmsisdn.startsWith("60601")) {
            rmsisdn = rmsisdn.substring(2);
        }
        if (rmsisdn.startsWith("6601")) {
            rmsisdn = rmsisdn.substring(1);
        }
        return rmsisdn;
    }

    private boolean validateDest(String custid, String rmsisdn) {
        try {
            String sql = rmsisdn.startsWith("601") ?
                    "SELECT local_flag FROM bulk_destination_sms WHERE custid = ? AND local_flag = '0'" :
                    "SELECT int_flag FROM bulk_destination_sms WHERE custid = ? AND int_flag = '0'";

            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);
            return !results.isEmpty();
        } catch (Exception e) {
            logger.error("Error validating destination: {}", e.getMessage());
            return true; // Default allow
        }
    }

    private boolean checkDataStr(String dataStr, int productType) {
        if (dataStr == null) return false;
        // Text message (productType 4 or 10) max 160 chars for GSM, 70 for unicode
        if (productType == 4 || productType == 10) {
            return dataStr.length() <= 918; // 6 concatenated messages
        }
        return true;
    }

    private boolean checkBlackWhiteList(String mtid, String rmsisdn, String shortcode, String keyword, String custid) {
        try {
            String sql = "SELECT message_id FROM sms_generic_forwardmo_blacklist " +
                    "WHERE RIGHT(msisdn, 9) = RIGHT(?, 9) AND blacklist_flag = 0";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, rmsisdn);
            return results.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking blacklist: {}", e.getMessage());
            return true;
        }
    }

    private boolean checkCredit(String custid) {
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

    private boolean checkMaskingID(String custid, String smsisdn) {
        try {
            String sql = "SELECT masking_id FROM bulk_masking_id WHERE custid = ? AND masking_id = ? AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid, smsisdn);
            return !results.isEmpty() || smsisdn.matches("^[0-9]+$"); // Allow numeric sender IDs
        } catch (Exception e) {
            logger.error("Error checking masking ID: {}", e.getMessage());
            return true;
        }
    }

    private boolean isDuplicateMessageId(String mtid, String custid) {
        try {
            String sql = "SELECT row_id FROM extmt_mtid WHERE mtid = ? AND custid = ?";
            List<Map<String, Object>> results = dbUtil.getGeneralJdbcTemplate().queryForList(sql, mtid, custid);
            return !results.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking duplicate message ID: {}", e.getMessage());
            return false;
        }
    }
}

