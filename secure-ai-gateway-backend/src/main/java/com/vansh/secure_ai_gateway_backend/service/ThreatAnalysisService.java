package com.vansh.secure_ai_gateway_backend.service;

import com.vansh.secure_ai_gateway_backend.repository.RequestLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThreatAnalysisService {

    private final RequestLogRepository requestLogRepository;

    public ThreatResult analyze(String ip, String endpoint) {

        double score;
        String label;

        if (endpoint.contains("malicious")) {
            score = 0.9;
        } else if (endpoint.contains("suspicious")) {
            score = 0.6;
        } else {
            score = 0.1;
        }

        if (score > 0.85) {
            label = "MALICIOUS";
        } else {
            long count = requestLogRepository
                    .countByClientIpAndEndpoint(ip, "/api/test/malicious");

            label = (count >= 5) ? "SUSPICIOUS" : "NORMAL";
        }

        return new ThreatResult(label, score);
    }
}
