package com.vansh.secure_ai_gateway_backend.controller;

import com.vansh.secure_ai_gateway_backend.model.RequestLog;
import com.vansh.secure_ai_gateway_backend.repository.RequestLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/analytics")
public class AdminAnalyticsController {

    private final RequestLogRepository requestLogRepository;

    public AdminAnalyticsController(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    @GetMapping("/summary")
    public Map<String, Long> summary() {
        Map<String, Long> res = new HashMap<>();
        res.put("total", requestLogRepository.count());
        res.put("normal", requestLogRepository.countByThreatLabel("NORMAL"));
        res.put("suspicious", requestLogRepository.countByThreatLabel("SUSPICIOUS"));
        res.put("malicious", requestLogRepository.countByThreatLabel("MALICIOUS"));
        return res;
    }

    @GetMapping("/recent")
    public List<RequestLog> recent() {
        return requestLogRepository.findTop20ByOrderByTimestampDesc();
    }

    @GetMapping("/by-endpoint")
    public List<Map<String, Object>> byEndpoint() {
        return requestLogRepository.aggregateByEndpoint();
    }
}
