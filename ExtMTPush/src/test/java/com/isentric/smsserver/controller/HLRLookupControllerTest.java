package com.isentric.smsserver.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HLRLookupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHLRLookup() throws Exception {
        mockMvc.perform(get("/api/hlr/lookup/60123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msisdn").value("60123456789"))
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void testGetNetworkOperator() throws Exception {
        mockMvc.perform(get("/api/hlr/network/60123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msisdn").value("60123456789"))
                .andExpect(jsonPath("$.operator").exists());
    }

    @Test
    public void testValidateMSISDN() throws Exception {
        mockMvc.perform(get("/api/hlr/validate/60123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msisdn").value("60123456789"))
                .andExpect(jsonPath("$.valid").exists());
    }

    @Test
    public void testBatchLookup() throws Exception {
        String batchRequest = "[\"60123456789\", \"60163456789\", \"60173456789\"]";

        mockMvc.perform(post("/api/hlr/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(batchRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    public void testHLRLookupMaxisMSISDN() throws Exception {
        mockMvc.perform(get("/api/hlr/network/60123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("MAXIS"));
    }

    @Test
    public void testHLRLookupDigiMSISDN() throws Exception {
        mockMvc.perform(get("/api/hlr/network/60163456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("DIGI"));
    }

    @Test
    public void testHLRLookupCelcomMSISDN() throws Exception {
        mockMvc.perform(get("/api/hlr/network/60193456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operator").value("CELCOM"));
    }
}

