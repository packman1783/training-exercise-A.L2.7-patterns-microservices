package org.example.gateway.service;

import java.util.Map;

import org.example.gateway.config.ExternalConfiguration;

import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ServiceDiscovery {
    private static final Logger log = LoggerFactory.getLogger(ServiceDiscovery.class);

    private final ExternalConfiguration externalConfiguration;

    public ServiceDiscovery(ExternalConfiguration externalConfiguration) {
        this.externalConfiguration = externalConfiguration;
        log.info("ServiceDiscovery initialized with endpoints: {}", externalConfiguration.getServices());
    }

    public String getServiceInstance(String serviceName) {
        Map<String, String> services = externalConfiguration.getServices();
        String url = services.get(serviceName);

        if(url == null) {
            log.error("NService not found: {}", serviceName);
            throw new IllegalArgumentException("Service not found: " + serviceName);
        }

        log.debug("Resolved service {} to {}", serviceName, url);
        return services.get(serviceName);
    }
}
