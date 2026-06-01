package com.vansh.secure_ai_gateway_backend.controller;

import com.vansh.secure_ai_gateway_backend.model.RequestLog;
import com.vansh.secure_ai_gateway_backend.repository.RequestLogRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/seed")
public class DataSeederController {

    private final RequestLogRepository requestLogRepository;

    public DataSeederController(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @PostMapping("/generate-data")
    public Map<String, Object> generateTestData(@RequestParam(defaultValue = "50") int count) {
        Random random = new Random();
        String[] endpoints = {
                "/api/test/normal",
                "/api/test/suspicious",
                "/api/test/malicious",
                "/admin/analytics/summary",
                "/auth/login",
                "/auth/register"
        };

        String[] usernames = {"testuser", "admin", "user1", "user2", "hacker", "bot"};
        String[] ips = {"192.168.1.100", "192.168.1.101", "10.0.0.1", "172.16.0.1", "203.0.113.45"};

        int normalCount = 0;
        int suspiciousCount = 0;
        int maliciousCount = 0;

        for (int i = 0; i < count; i++) {
            String threatLabel;
            double threatScore;
            String reason;

            // 60% Normal, 25% Suspicious, 15% Malicious
            int random_val = random.nextInt(100);
            if (random_val < 60) {
                threatLabel = "NORMAL";
                threatScore = 0.1 + (random.nextDouble() * 0.3);
                reason = "Normal API request";
                normalCount++;
            } else if (random_val < 85) {
                threatLabel = "SUSPICIOUS";
                threatScore = 0.4 + (random.nextDouble() * 0.3);
                reason = "Suspicious activity detected - unusual request pattern";
                suspiciousCount++;
            } else {
                threatLabel = "MALICIOUS";
                threatScore = 0.7 + (random.nextDouble() * 0.3);
                reason = "Malicious activity detected - potential attack";
                maliciousCount++;
            }

            RequestLog log = new RequestLog(
                    usernames[random.nextInt(usernames.length)],
                    endpoints[random.nextInt(endpoints.length)],
                    ips[random.nextInt(ips.length)],
                    LocalDateTime.now().minusMinutes(random.nextInt(1440)),
                    threatScore,
                    threatLabel,
                    reason
            );

            requestLogRepository.save(log);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Test data generated successfully");
        response.put("total_generated", count);
        response.put("normal", normalCount);
        response.put("suspicious", suspiciousCount);
        response.put("malicious", maliciousCount);
        response.put("timestamp", LocalDateTime.now());

        return response;
    }

    @DeleteMapping("/clear-data")
    public Map<String, Object> clearAllData() {
        long count = requestLogRepository.count();
        requestLogRepository.deleteAll();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "All data cleared successfully");
        response.put("deleted_count", count);
        response.put("timestamp", LocalDateTime.now());

        return response;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        long total = requestLogRepository.count();
        long normal = requestLogRepository.countByThreatLabel("NORMAL");
        long suspicious = requestLogRepository.countByThreatLabel("SUSPICIOUS");
        long malicious = requestLogRepository.countByThreatLabel("MALICIOUS");

        Map<String, Object> response = new HashMap<>();
        response.put("total", total);
        response.put("normal", normal);
        response.put("suspicious", suspicious);
        response.put("malicious", malicious);
        response.put("timestamp", LocalDateTime.now());

        return response;
    }
}
