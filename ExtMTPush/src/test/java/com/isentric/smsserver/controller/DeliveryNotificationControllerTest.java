package com.isentric.smsserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeliveryNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCelcomDN_GET() throws Exception {
        mockMvc.perform(get("/dn/celcom")
                        .param("SMP_Txid", "TX123456")
                        .param("SUB_Mobtel", "60133456789")
                        .param("APIType", "HTTP")
                        .param("SMP_Keyword", "TEST")
                        .param("SMP_ServiceID", "SVC001")
                        .param("DNStatus", "DELIVERED")
                        .param("SMS_SourceAddr", "12345")
                        .param("ErrorCode", "DeliveredToTerminal"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testCelcomDN_POST() throws Exception {
        mockMvc.perform(post("/dn/celcom")
                        .param("SMP_Txid", "TX123456")
                        .param("SUB_Mobtel", "60133456789")
                        .param("ErrorCode", "DeliveredToTerminal"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testCelcomDN_Undeliverable() throws Exception {
        mockMvc.perform(get("/dn/celcom")
                        .param("SMP_Txid", "TX789012")
                        .param("SUB_Mobtel", "60133456789")
                        .param("ErrorCode", "DeliveryImpossible"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testDigiDN() throws Exception {
        mockMvc.perform(post("/dn/digi")
                        .param("messageId", "MSG123456")
                        .param("status", "DELIVERED")
                        .param("msisdn", "60163456789")
                        .param("deliveryTime", "2026-01-06 10:30:00")
                        .param("errorCode", "0"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testDigiDN_Undelivered() throws Exception {
        mockMvc.perform(post("/dn/digi")
                        .param("messageId", "MSG789012")
                        .param("status", "UNDELIVERED")
                        .param("msisdn", "60163456789")
                        .param("errorCode", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testMaxisDN() throws Exception {
        mockMvc.perform(post("/dn/maxis")
                        .param("messageId", "MSG345678")
                        .param("status", "DELIVRD")
                        .param("msisdn", "60173456789")
                        .param("deliveryTime", "2026-01-06 11:00:00")
                        .param("errorCode", "0"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testMaxisDN_Expired() throws Exception {
        mockMvc.perform(post("/dn/maxis")
                        .param("messageId", "MSG567890")
                        .param("status", "EXPIRED")
                        .param("msisdn", "60173456789"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testSilverStreetDN() throws Exception {
        mockMvc.perform(post("/dn/silverstreet")
                        .param("messageId", "MSG901234")
                        .param("status", "1")
                        .param("msisdn", "60123456789")
                        .param("errorCode", "0"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }

    @Test
    public void testGenericDN_GET() throws Exception {
        mockMvc.perform(get("/dn/umobile")
                        .param("messageId", "MSG111222")
                        .param("status", "DELIVERED")
                        .param("msisdn", "60183456789")
                        .param("errorCode", "0"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"));
    }
}

