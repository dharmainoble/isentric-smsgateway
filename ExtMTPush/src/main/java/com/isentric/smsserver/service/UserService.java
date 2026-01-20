package com.isentric.smsserver.service;

import com.isentric.smsserver.object.UserObject;
import com.isentric.smsserver.util.DBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * User management service
 * Based on original CheckSMSUser from ExtMTPush.jar
 */
@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final DBUtil dbUtil;

    @Autowired
    public UserService(DBUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    /**
     * Check SMS User - validate user credentials and permissions (from original CheckSMSUser)
     */
    public boolean checkSMSUser(String custid, String password, String ipAddress) {
        logger.info("Checking SMS user: {}, IP: {}", custid, ipAddress);

        try {
            // Check user exists and is active
            String sql = "SELECT custid, password, status FROM bulk_credit WHERE custid = ? AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);

            if (results.isEmpty()) {
                logger.warn("User not found: {}", custid);
                return false;
            }

            Map<String, Object> row = results.get(0);
            String storedPassword = getString(row, "password");

            // Validate password
            if (password != null && !password.isEmpty()) {
                if (!password.equals(storedPassword)) {
                    logger.warn("Invalid password for user: {}", custid);
                    return false;
                }
            }

            // Check IP whitelist if configured
            if (ipAddress != null && !ipAddress.isEmpty()) {
                if (!isIPAllowed(custid, ipAddress)) {
                    logger.warn("IP not allowed for user: {}, IP: {}", custid, ipAddress);
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            logger.error("Error checking SMS user: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Check if IP is allowed for user (from original IP validation logic)
     */
    public boolean isIPAllowed(String custid, String ipAddress) {
        try {
            String sql = "SELECT ip_address FROM bulk_ip_whitelist WHERE custid = ? AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);

            // If no IP whitelist configured, allow all
            if (results.isEmpty()) {
                return true;
            }

            // Check if IP is in whitelist
            for (Map<String, Object> row : results) {
                String allowedIP = getString(row, "ip_address");
                if (ipAddress.equals(allowedIP) || "0.0.0.0".equals(allowedIP)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            logger.error("Error checking IP whitelist: {}", e.getMessage());
            return true; // Default allow on error
        }
    }

    /**
     * Get user by customer ID
     */
    public UserObject getUserByCustid(String custid) {
        logger.info("Getting user by custid: {}", custid);

        try {
            String sql = "SELECT * FROM bulk_credit WHERE custid = ?";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);

            if (!results.isEmpty()) {
                return mapRowToUserObject(results.get(0));
            }
            return null;
        } catch (Exception e) {
            logger.error("Error getting user: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get user by user ID
     */
    public UserObject getUserById(String userId) {
        logger.info("Getting user by ID: {}", userId);

        try {
            JdbcTemplate jdbcTemplate = dbUtil.getAvatarJdbcTemplate();
            String sql = "SELECT * FROM bulk_credit WHERE custid = ?";

            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, userId);

            if (!results.isEmpty()) {
                return mapRowToUserObject(results.get(0));
            }

            return null;
        } catch (Exception e) {
            logger.error("Error getting user: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get user by username
     */
    public UserObject getUserByUsername(String username) {
        logger.info("Getting user by username: {}", username);

        try {
            JdbcTemplate jdbcTemplate = dbUtil.getAvatarJdbcTemplate();
            String sql = "SELECT * FROM bulk_credit WHERE custid = ?";

            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, username);

            if (!results.isEmpty()) {
                return mapRowToUserObject(results.get(0));
            }

            return null;
        } catch (Exception e) {
            logger.error("Error getting user: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Authenticate user
     */
    public boolean authenticate(String username, String password) {
        logger.info("Authenticating user: {}", username);

        try {
            return checkSMSUser(username, password, null);
        } catch (Exception e) {
            logger.error("Error authenticating user: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Check if user is active
     */
    public boolean isUserActive(String userId) {
        try {
            String sql = "SELECT status FROM bulk_credit WHERE custid = ? AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, userId);
            return !results.isEmpty();
        } catch (Exception e) {
            logger.error("Error checking user status: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get masking IDs for user (from original getMaskingIDs)
     */
    public List<Map<String, Object>> getMaskingIDs(String custid) {
        try {
            String sql = "SELECT masking_id FROM bulk_masking_id WHERE custid = ? AND status = 1";
            return dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid);
        } catch (Exception e) {
            logger.error("Error getting masking IDs: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Check if masking ID is valid for user (from original checkMaskingID)
     */
    public boolean isValidMaskingID(String custid, String maskingId) {
        try {
            String sql = "SELECT masking_id FROM bulk_masking_id WHERE custid = ? AND masking_id = ? AND status = 1";
            List<Map<String, Object>> results = dbUtil.getAvatarJdbcTemplate().queryForList(sql, custid, maskingId);
            // Allow numeric sender IDs without registration
            return !results.isEmpty() || maskingId.matches("^[0-9]+$");
        } catch (Exception e) {
            logger.error("Error checking masking ID: {}", e.getMessage());
            return false;
        }
    }

    private UserObject mapRowToUserObject(Map<String, Object> row) {
        UserObject user = new UserObject();
        user.setUserId(getString(row, "custid"));
        user.setUsername(getString(row, "custid"));
        user.setCustid(getString(row, "custid"));
        user.setStatus(row.get("status") != null ? row.get("status").toString() : "1");
        return user;
    }

    private String getString(Map<String, Object> row, String key) {
        Object value = row.get(key);
        return value != null ? value.toString() : "";
    }
}
