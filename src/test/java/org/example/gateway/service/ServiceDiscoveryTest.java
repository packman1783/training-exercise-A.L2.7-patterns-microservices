package org.example.gateway.service;

import org.example.gateway.config.ExternalConfiguration;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServiceDiscoveryTest {
    @Test
    void shouldReturnCorrectServiceUrl() {
        ExternalConfiguration config = new ExternalConfiguration();
        config.setServices(Map.of("user-service", "http://localhost:8080"));

        ServiceDiscovery discovery = new ServiceDiscovery(config);

        assertEquals("http://localhost:8080", discovery.getServiceInstance("user-service"));
    }

    @Test
    void shouldReturnNullForUnknownService() {
        ExternalConfiguration config = new ExternalConfiguration();
        config.setServices(Map.of("user-service", "http://localhost:8080"));

        ServiceDiscovery discovery = new ServiceDiscovery(config);

        assertNull(discovery.getServiceInstance("notification-service"));
    }
}
