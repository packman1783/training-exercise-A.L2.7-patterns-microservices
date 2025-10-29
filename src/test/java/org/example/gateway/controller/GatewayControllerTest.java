package org.example.gateway.controller;

import org.example.gateway.service.CircuitBreaker;
import org.example.gateway.service.ServiceDiscovery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GatewayController.class)
class GatewayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServiceDiscovery discovery;

    @MockitoBean
    private CircuitBreaker breaker;

    @BeforeEach
    void setup() {
        when(discovery.getServiceInstance("user-service")).thenReturn("http://localhost:8080");
        when(discovery.getServiceInstance("notification-service")).thenReturn("http://localhost:8081");
    }

    @Test
    void shouldReturnUserResponse() throws Exception {
        when(breaker.get(anyString(), anyString())).thenReturn("{\"name\":\"Alice\"}");

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Alice\"}"));
    }

    @Test
    void shouldReturnNotificationFallback() throws Exception {
        when(breaker.post(anyString(), any(), anyString()))
                .thenReturn("{\"error\":\"Notification service unavailable\"}");

        mockMvc.perform(post("/api/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"msg\":\"hi\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"error\":\"Notification service unavailable\"}"));
    }
}
