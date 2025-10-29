package org.example.gateway.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
public class ExternalConfiguration {
    private Map<String, String> services;

    public Map<String, String> getServices() {
        return services;
    }

    public void setServices(Map<String, String> services) {
        this.services = services;
    }
}
