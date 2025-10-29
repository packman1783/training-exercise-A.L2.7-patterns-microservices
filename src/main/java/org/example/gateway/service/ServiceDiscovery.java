package org.example.gateway.service;

import java.util.Map;

import org.example.gateway.config.ExternalConfiguration;

import org.springframework.stereotype.Component;

@Component
public class ServiceDiscovery {
    private final ExternalConfiguration externalConfiguration;

    public ServiceDiscovery(ExternalConfiguration externalConfiguration) {
        this.externalConfiguration = externalConfiguration;
    }

    public String getServiceInstance(String serviceName) {
        Map<String, String> services = externalConfiguration.getServices();

        return services.get(serviceName);
    }
}
