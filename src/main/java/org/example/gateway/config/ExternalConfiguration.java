package org.example.gateway.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "services")
public class ExternalConfiguration {
    private Map<String, List<String>> services;

    public Map<String, List<String>> getServices() {
        return services;
    }

    public void setServices(Map<String, List<String>> services) {
        this.services = services;
    }
}
