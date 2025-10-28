package org.example.gateway.service;

import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.example.gateway.config.ExternalConfiguration;
import org.springframework.stereotype.Component;

//String url = serviceRegistry.getServiceInstance("notification-service") + "/notification/send";

@Component
public class ServiceDiscovery {
    private final ExternalConfiguration externalConfiguration;
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public ServiceDiscovery(ExternalConfiguration externalConfiguration) {
        this.externalConfiguration = externalConfiguration;
    }

    public String getServiceInstance(String serviceName) {
        Map<String, List<String>> services = externalConfiguration.getServices();

        List<String> instances = services.get(serviceName);

        if (instances == null || instances.isEmpty()) {
            return null;
        }

        counters.putIfAbsent(serviceName, new AtomicInteger(0));
        int index = counters.get(serviceName).getAndIncrement() % instances.size();

        return instances.get(index);
    }
}
