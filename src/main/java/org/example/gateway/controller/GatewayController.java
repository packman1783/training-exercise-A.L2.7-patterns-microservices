package org.example.gateway.controller;

import java.util.Map;

import org.example.gateway.service.CircuitBreaker;
import org.example.gateway.service.ServiceDiscovery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class GatewayController {
    private static final Logger log = LoggerFactory.getLogger(GatewayController.class);

    private final ServiceDiscovery serviceDiscovery;
    private final CircuitBreaker circuitBreaker;

    public GatewayController(ServiceDiscovery serviceDiscovery, CircuitBreaker circuitBreaker) {
        this.serviceDiscovery = serviceDiscovery;
        this.circuitBreaker = circuitBreaker;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<String> getUser(@PathVariable String id) {
        String url = serviceDiscovery.getServiceInstance("user-service") + "/users/" + id;
        log.info("-> Forwarding GET request to UserService: {}", url);

        String response = circuitBreaker.get(url, "{\"error\":\"User service unavailable\"}");

        log.info("<- Response from UserService: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notification")
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, Object> body) {
        String url = serviceDiscovery.getServiceInstance("notification-service") + "/notification/send";
        log.info("-> Forwarding POST request to NotificationService: {}", url);

        String response = circuitBreaker.post(url, body, "{\"error\":\"Notification service unavailable\"}");

        log.info("<- Response from NotificationService: {}", response);
        return ResponseEntity.ok(response);
    }
}
