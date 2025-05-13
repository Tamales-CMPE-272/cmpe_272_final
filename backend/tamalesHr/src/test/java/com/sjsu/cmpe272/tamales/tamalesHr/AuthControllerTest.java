package com.sjsu.cmpe272.tamales.tamalesHr;

import com.sjsu.cmpe272.tamales.tamalesHr.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private RestTemplate restTemplate;

    @Test
    void testGetToken_Success() throws Exception {
        String fakeTokenResponse = "{\"access_token\":\"fake-jwt-token\"}";
        ResponseEntity<String> fakeResponse = new ResponseEntity<>(fakeTokenResponse, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), Mockito.eq(String.class)))
                .thenReturn(fakeResponse);

        mockMvc.perform(post("/auth/token")
                        .param("username", "john.doe")
                        .param("password", "Password123"))
                .andExpect(status().isOk())
                .andExpect(content().string(fakeTokenResponse));
    }

    @Test
    void testGetToken_Failure() throws Exception {
        Mockito.when(restTemplate.postForEntity(any(String.class), any(), Mockito.eq(String.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/auth/token")
                        .param("username", "john.doe")
                        .param("password", "WrongPassword"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login failed")));
    }
}