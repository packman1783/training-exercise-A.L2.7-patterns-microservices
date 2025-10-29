package org.example.gateway.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircuitBreakerTest {
    @Test
    void shouldReturnFallbackWhenUrlIsInvalid() {
        CircuitBreaker breaker = new CircuitBreaker();

        String result = breaker.get("http://localhost:9999/unknown", "{\"error\":\"fallback\"}");

        assertEquals("{\"error\":\"fallback\"}", result);
    }

    @Test
    void shouldTriggerCircuitAfterFailures() {
        CircuitBreaker breaker = new CircuitBreaker();
        String url = "http://localhost:9999/fail";

        for (int i = 0; i < 4; i++) {
            breaker.get(url, "fallback");
        }

        String response = breaker.get(url, "fallback");

        assertEquals("fallback", response);
    }
}

