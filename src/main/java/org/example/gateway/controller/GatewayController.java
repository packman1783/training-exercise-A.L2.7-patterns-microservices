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

@RestController
@RequestMapping("/api")
public class GatewayController {
    private final ServiceDiscovery serviceDiscovery;
    private final CircuitBreaker circuitBreaker;

    public GatewayController(ServiceDiscovery serviceDiscovery, CircuitBreaker circuitBreaker) {
        this.serviceDiscovery = serviceDiscovery;
        this.circuitBreaker = circuitBreaker;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<String> getUser(@PathVariable String id) {
        String url = serviceDiscovery.getServiceInstance("user-service") + "/users/" + id;
        String response = circuitBreaker.get(url, "{\"error\":\"User service unavailable\"}");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/notification")
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, Object> body) {
        String url = serviceDiscovery.getServiceInstance("notification-service") + "/notification/send";
        String response = circuitBreaker.post(url, body, "{\"error\":\"Notification service unavailable\"}");

        return ResponseEntity.ok(response);
    }
}
