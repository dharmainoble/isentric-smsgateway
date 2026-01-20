package com.isentric.smsserver.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SmsUtilTest {
    
    @Test
    void testNormalizeMsisdn() {
        // Test local format conversion
        assertEquals("60123456789", SmsUtil.normalizeMsisdn("0123456789"));
        assertEquals("60123456789", SmsUtil.normalizeMsisdn("60123456789"));
        
        // Test with + prefix
        assertEquals("60123456789", SmsUtil.normalizeMsisdn("+60123456789"));
        
        // Test with 00 prefix
        assertEquals("60123456789", SmsUtil.normalizeMsisdn("0060123456789"));
    }
    
    @Test
    void testGetTelco() {
        // Test Celcom
        assertEquals("CELCOM", SmsUtil.getTelco("60198765432"));
        
        // Test Digi
        assertEquals("DIGI", SmsUtil.getTelco("60168765432"));
        
        // Test Maxis
        assertEquals("MAXIS", SmsUtil.getTelco("60128765432"));
        
        // Test U Mobile
        assertEquals("UMOBILE", SmsUtil.getTelco("60188765432"));
        
        // Test unknown
        assertEquals("UNKNOWN", SmsUtil.getTelco("60998765432"));
    }
    
    @Test
    void testIsValidMsisdn() {
        // Valid MSISDNs
        assertTrue(SmsUtil.isValidMsisdn("60123456789"));
        assertTrue(SmsUtil.isValidMsisdn("601234567890"));
        
        // Invalid MSISDNs
        assertFalse(SmsUtil.isValidMsisdn(null));
        assertFalse(SmsUtil.isValidMsisdn(""));
        assertFalse(SmsUtil.isValidMsisdn("123"));
    }
    
    @Test
    void testIsLocalMsisdn() {
        // Local numbers
        assertTrue(SmsUtil.isLocalMsisdn("60123456789"));
        assertTrue(SmsUtil.isLocalMsisdn("0123456789"));
        
        // International numbers
        assertFalse(SmsUtil.isLocalMsisdn("8612345678"));
    }
    
    @Test
    void testRemoveSpecialChars() {
        assertEquals("Test''s Message", SmsUtil.removeSpecialChars("Test's Message"));
        assertEquals("C:\\\\Users", SmsUtil.removeSpecialChars("C:\\Users"));
    }
}

