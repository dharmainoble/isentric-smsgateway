package com.isentric.smsserver.service;

import com.isentric.smsserver.object.CreditObject;
import com.isentric.smsserver.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Credit management service
 * Based on original GenericCreditSoapBindingImpl from ExtMTPush.jar
 */
@Service
public class CreditService {

    private static final Logger logger = LoggerFactory.getLogger(CreditService.class);

    // Credit type constants from original code
    private static final int CREDIT_TYPE_UNLIMITED = -1;
    private static final int CREDIT_TYPE_PREPAID = 1;

    private final DBUtil dbUtil;

    @Autowired
    public CreditService(DBUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    /**
     * Get user credit by customer ID (from original checkCredit)
     */
    public CreditObject getUserCredit(String custid) {
        logger.info("Getting credit for custid: {}", custid);

        try {
            JdbcTemplate jdbcTemplate = dbUtil.getAvatarJdbcTemplate();
            String sql = "SELECT * FROM bulk_credit WHERE custid = ? AND status = 1";

            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, custid);

            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                CreditObject credit = new CreditObject();
                credit.setCustid(custid);
                credit.setCredit(getDouble(row, "credit"));
                credit.setCreditType(getInt(row, "credit_type"));
                credit.setStatus(getString(row, "status"));

                // For unlimited credit, set remaining as max
                if (credit.getCreditType() == CREDIT_TYPE_UNLIMITED) {
                    credit.setRemainingCredit(Double.MAX_VALUE);
                } else {
                    credit.setRemainingCredit(credit.getCredit());
                }
                return credit;
            }

            return null;
        } catch (Exception e) {
            logger.error("Error getting user credit: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Check if user has sufficient credit (from original checkCredit in BulkExtMTPushReceiveUtil)
     */
    public boolean hasCredit(String custid) {
        try {
            String sql = "SELECT credit_type FROM bulk_credit WHERE custid = ? " +
                    "AND ((credit_type = -1) OR (credit_type = 1 AND credit > 1)) AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);
            return !results.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking credit: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Deduct credit from user account (from original deductCredit)
     */
    public boolean deductCredit(String custid, double amount) {
        logger.info("Deducting credit for custid: {}, amount: {}", custid, amount);

        try {
            // Check if user has credit
            CreditObject currentCredit = getUserCredit(custid);
            if (currentCredit == null) {
                logger.error("User not found: {}", custid);
                return false;
            }

            // Unlimited credit - no deduction needed
            if (currentCredit.getCreditType() == CREDIT_TYPE_UNLIMITED) {
                logger.debug("Unlimited credit for custid: {}, no deduction", custid);
                return true;
            }

            // Check sufficient credit
            if (currentCredit.getCredit() < amount) {
                logger.error("Insufficient credit for custid: {}", custid);
                return false;
            }

            // Deduct credit
            String sql = "UPDATE bulk_credit SET credit = credit - ? WHERE custid = ? AND credit >= ? AND status = 1";
            int updated = dbUtil.getAvatarJdbcTemplate().update(sql, amount, custid, amount);

            if (updated > 0) {
                // Log credit transaction
                logCreditTransaction(custid, -amount, "DEDUCT", "SMS send");
            }

            return updated > 0;
        } catch (Exception e) {
            logger.error("Error deducting credit: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Add credit to user account (from original addCredit)
     */
    public boolean addCredit(String custid, double amount) {
        logger.info("Adding credit for custid: {}, amount: {}", custid, amount);

        try {
            String sql = "UPDATE bulk_credit SET credit = credit + ? WHERE custid = ? AND status = 1";
            int updated = dbUtil.getAvatarJdbcTemplate().update(sql, amount, custid);

            if (updated > 0) {
                // Log credit transaction
                logCreditTransaction(custid, amount, "ADD", "Credit top-up");
            }

            return updated > 0;
        } catch (Exception e) {
            logger.error("Error adding credit: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Log credit transaction (from original logCreditTransaction)
     */
    private void logCreditTransaction(String custid, double amount, String type, String remarks) {
        try {
            String sql = "INSERT INTO bulk_credit_log (custid, amount, transaction_type, remarks, transaction_date) " +
                    "VALUES (?, ?, ?, ?, NOW())";
            dbUtil.getAvatarJdbcTemplate().update(sql, custid, amount, type, remarks);
        } catch (Exception e) {
            logger.error("Error logging credit transaction: {}", e.getMessage());
        }
    }

    /**
     * Get credit balance
     */
    public double getCreditBalance(String custid) {
        CreditObject credit = getUserCredit(custid);
        if (credit != null) {
            if (credit.getCreditType() == CREDIT_TYPE_UNLIMITED) {
                return -1; // Unlimited
            }
            return credit.getCredit();
        }
        return 0.0;
    }

    /**
     * Check if customer is allowed to send (from original checkSMSUser)
     */
    public boolean isAllowedToSend(String custid) {
        try {
            String sql = "SELECT status FROM bulk_credit WHERE custid = ? AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);
            return !results.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking send permission: {}", e.getMessage());
            return false;
        }
    }

    // Helper methods
    private String getString(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString() : "";
    }

    private double getDouble(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    private int getInt(Map<String, Object> row, String key) {
        Object value = row.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }
}
