package com.isentric.smsserver.service;

import com.isentric.smsserver.repository.general.CpIpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ValidationService {
    
    private final CpIpRepository cpIpRepository;
    private final JdbcTemplate jdbcTemplate;
    
    /**
     * Validate package/IP authorization
     */
    @Cacheable(value = "routeCache", key = "#shortcode + '_' + #custid + '_' + #ipaddr")
    public boolean validatePackage(String shortcode, String custid, String ipaddr) {
        try {
            log.info("Validating package for shortcode: {}, custid: {}, ipaddr: {}", shortcode, custid, ipaddr);
            return cpIpRepository.findByShortcodeAndCpidentityAndCpIp(shortcode, custid, ipaddr)
                    .map(cpIp -> "1".equals(cpIp.getHlrFlag()))
                    .orElse(false);
        } catch (Exception e) {
            log.error("Error validating package", e);
            return false;
        }
    }
    
    /**
     * Validate destination number
     */
    public boolean validateDestination(String custid, String rmsisdn) {
        try {
            boolean isLocal = rmsisdn.startsWith("601");
            String flagColumn = isLocal ? "local_flag" : "int_flag";
            
            String sql = "SELECT " + flagColumn + " FROM bulk_config.bulk_destination_sms " +
                        "WHERE custid = ? AND " + flagColumn + " = '0'";
            
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, custid);
            return !results.isEmpty();
            
        } catch (Exception e) {
            log.error("Error validating destination", e);
            return false;
        }
    }
    
    /**
     * Check customer credit
     */
    @Cacheable(value = "creditCache", key = "#custid")
    public boolean checkCredit(String custid) {
        try {
            String sql = "SELECT credit_balance FROM bulk_config.customer_credit WHERE custid = ?";
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, custid);
            
            if (results.isEmpty()) {
                return false;
            }
            
            Object balance = results.get(0).get("credit_balance");
            if (balance instanceof Number) {
                return ((Number) balance).doubleValue() > 0;
            }
            
            return false;
        } catch (Exception e) {
            log.error("Error checking credit for custid: {}", custid, e);
            return false;
        }
    }
    
    /**
     * Check blacklist/whitelist
     */
    @Cacheable(value = "blacklistCache", key = "#msisdn + '_' + #shortcode")
    public boolean checkBlackWhiteList(String mtid, String msisdn, String shortcode, 
                                       String keyword, String custid) {
        try {
            // Check blacklist
            String blacklistSql = "SELECT COUNT(*) FROM bulk_config.blacklist " +
                                 "WHERE msisdn = ? AND shortcode = ?";
            Integer blacklistCount = jdbcTemplate.queryForObject(blacklistSql, Integer.class, msisdn, shortcode);
            
            if (blacklistCount != null && blacklistCount > 0) {
                log.warn("MSISDN {} is blacklisted for shortcode {}", msisdn, shortcode);
                return false;
            }
            
            // Additional validation can be added here
            return true;
            
        } catch (Exception e) {
            log.error("Error checking blacklist/whitelist", e);
            return false;
        }
    }
    
    /**
     * Check masking ID validity
     */
    public boolean checkMaskingId(String custid, String maskingId) {
        try {
            if (maskingId == null || maskingId.isEmpty()) {
                return true; // Optional field
            }
            
            String sql = "SELECT COUNT(*) FROM bulk_config.masking_id " +
                        "WHERE custid = ? AND masking_id = ? AND active = '1'";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, custid, maskingId);
            
            return count != null && count > 0;
            
        } catch (Exception e) {
            log.error("Error checking masking ID", e);
            return false;
        }
    }
    
    /**
     * Validate product code
     */
    public boolean validateProductCode(String productCode, int productType, String msisdn) {
        try {
            String tableName = getContentTable(productType, msisdn);
            String sql = "SELECT COUNT(*) FROM content_db." + tableName + " WHERE code = ?";
            
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, productCode);
            return count != null && count > 0;
            
        } catch (Exception e) {
            log.error("Error validating product code", e);
            return false;
        }
    }
    
    /**
     * Get content table name based on product type
     */
    private String getContentTable(int productType, String msisdn) {
        String telco = getTelcoPrefix(msisdn);
        
        return switch (productType) {
            case 0, 1 -> "sms_ringtones";
            case 2 -> switch (telco) {
                case "6013" -> "sms_logos_tm";
                case "6016", "60146" -> "sms_logos_digi";
                case "6019" -> "sms_logos_celcom";
                default -> "sms_logos";
            };
            case 3 -> "sms_pictures";
            case 5 -> "sms_wap_ringtones";
            case 6 -> "sms_wap_truetones";
            case 7 -> "sms_wap_pictures";
            case 8 -> "sms_wap_games";
            case 11 -> "sms_wap_themes";
            case 12 -> "sms_wap_ebooks";
            case 13 -> "sms_wap_animations";
            case 14 -> "sms_wap_karaokes";
            case 15 -> "sms_wap_videos";
            default -> "sms_content";
        };
    }
    
    /**
     * Get telco prefix from MSISDN
     */
    private String getTelcoPrefix(String msisdn) {
        if (msisdn.length() >= 4) {
            return msisdn.substring(0, 4);
        }
        return "";
    }
}

