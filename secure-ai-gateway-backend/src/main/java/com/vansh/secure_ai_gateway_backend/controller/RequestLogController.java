// src/main/java/com/vansh/secure_ai_gateway_backend/controller/RequestLogController.java
package com.vansh.secure_ai_gateway_backend.controller;

import com.vansh.secure_ai_gateway_backend.model.RequestLog;
import com.vansh.secure_ai_gateway_backend.repository.RequestLogRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
public class RequestLogController {

    private final RequestLogRepository repo;

    public RequestLogController(RequestLogRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/normal")
    public String normalRequest(Authentication auth) {
        String username = auth != null ? auth.getName() : "anonymous";

        RequestLog log = new RequestLog();
        log.setUsername(username);
        log.setEndpoint("/api/test/normal");
        log.setThreatLabel("NORMAL");
        log.setTimestamp(LocalDateTime.now());
        log.setThreatScore(0.12);

        repo.save(log);
        return "Normal API OK!";
    }

    @GetMapping("/suspicious")
    public String suspiciousRequest(Authentication auth) {
        String username = auth != null ? auth.getName() : "anonymous";

        RequestLog log = new RequestLog();
        log.setUsername(username);
        log.setEndpoint("/api/test/suspicious");
        log.setThreatLabel("SUSPICIOUS");
        log.setTimestamp(LocalDateTime.now());
        log.setThreatScore(0.62);

        repo.save(log);
        return "Suspicious Activity Detected!";
    }

    @GetMapping("/malicious")
    public String maliciousRequest(Authentication auth) {
        String username = auth != null ? auth.getName() : "anonymous";

        RequestLog log = new RequestLog();
        log.setUsername(username);
        log.setEndpoint("/api/test/malicious");
        log.setThreatLabel("MALICIOUS");
        log.setTimestamp(LocalDateTime.now());
        log.setThreatScore(0.91);

        repo.save(log);
        return "🚨 Malicious Threat Logged!";
    }
}
