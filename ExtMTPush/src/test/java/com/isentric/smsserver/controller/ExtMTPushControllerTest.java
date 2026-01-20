package com.isentric.smsserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExtMTPushControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/sms/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.application").value("ExtMTPush"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void testSendSMS() throws Exception {
        String smsRequest = """
            {
                "shortcode": "12345",
                "custid": "TEST001",
                "rmsisdn": "60123456789",
                "smsisdn": "SENDER",
                "mtid": "MSG001",
                "price": "000",
                "dataStr": "Test message",
                "keyword": "TEST"
            }
            """;

        mockMvc.perform(post("/api/sms/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(smsRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void testSendBulkSMS() throws Exception {
        String bulkRequest = """
            {
                "billFlag": "1",
                "shortcode": "12345",
                "custid": "TEST001",
                "rmsisdn": "60123456789",
                "smsisdn": "SENDER",
                "mtid": "BULK001",
                "mtprice": "000",
                "productType": 4,
                "dataEncoding": 0,
                "dataStr": "Test bulk message",
                "dnrep": 1
            }
            """;

        mockMvc.perform(post("/api/sms/bulk/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bulkRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void testCheckDeliveryStatus() throws Exception {
        mockMvc.perform(get("/api/sms/status/MSG001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mtid").value("MSG001"))
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void testGetUserCredit() throws Exception {
        mockMvc.perform(get("/api/sms/credit/TEST001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.custid").value("TEST001"))
                .andExpect(jsonPath("$.credit").exists());
    }

    @Test
    public void testDeductCredit() throws Exception {
        mockMvc.perform(post("/api/sms/credit/deduct")
                        .param("custid", "TEST001")
                        .param("amount", "1.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.custid").value("TEST001"))
                .andExpect(jsonPath("$.amount").value(1.0))
                .andExpect(jsonPath("$.success").exists());
    }
}

