package com.dmt.bankingapp.springTests.controllerTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAccessTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accessToTestEndpointWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/test/someEndpoint"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void accessToTestEndpointWithAuthentication() throws Exception {
        mockMvc.perform(get("/test/someEndpoint"))
                .andExpect(status().isOk());
    }
}
