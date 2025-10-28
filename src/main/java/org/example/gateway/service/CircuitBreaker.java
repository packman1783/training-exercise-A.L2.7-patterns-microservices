package org.example.gateway.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CircuitBreaker {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ConcurrentHashMap<String, AtomicInteger> failures = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> openUntil = new ConcurrentHashMap<>();
    private final int failureThreshold = 3;
    private final long resetTimeMs = 5000;

    public String get(String url, String fallback) {
        if (isOpen(url)) {
            System.out.println("[CIRCUIT OPEN] Skipping GET " + url);
            return fallback;
        }

        try {
            String result = restTemplate.getForObject(url, String.class);
            resetFailures(url);
            return result;
        } catch (RestClientException e) {
            registerFailure(url, e);
            return fallback;
        }
    }

    public String post(String url, Object body, String fallback) {
        if (isOpen(url)) {
            System.out.println("[CIRCUIT OPEN] Skipping POST " + url);
            return fallback;
        }

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);
            resetFailures(url);
            return response.getBody();
        } catch (RestClientException e) {
            registerFailure(url, e);
            return fallback;
        }
    }

    private boolean isOpen(String url) {
        Long until = openUntil.get(url);

        if (until == null) return false;

        if (System.currentTimeMillis() > until) {
            openUntil.remove(url);
            failures.put(url, new AtomicInteger(0));
            System.out.println("[CIRCUIT RESET] " + url);
            return false;
        }
        return true;
    }

    private void registerFailure(String url, Exception e) {
        failures.putIfAbsent(url, new AtomicInteger(0));

        int count = failures.get(url).incrementAndGet();
        System.out.printf("[FAILURE] %s (count=%d): %s%n", url, count, e.getMessage());

        if (count >= failureThreshold) {
            openUntil.put(url, System.currentTimeMillis() + resetTimeMs);
            System.out.println("[CIRCUIT OPENED] for " + url);
        }
    }

    private void resetFailures(String url) {
        failures.put(url, new AtomicInteger(0));
    }
}
