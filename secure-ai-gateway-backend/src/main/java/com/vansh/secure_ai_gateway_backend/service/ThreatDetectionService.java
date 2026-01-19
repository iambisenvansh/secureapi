package com.vansh.secure_ai_gateway_backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vansh.secure_ai_gateway_backend.model.ThreatAnalysisResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ThreatDetectionService {

    private final RestClient openAiClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String model;

    public ThreatDetectionService(RestClient openAiRestClient,
                                  @Value("${openai.model}") String model) {
        this.openAiClient = openAiRestClient;
        this.model = model;
    }

    public ThreatAnalysisResult analyze(String username, String endpoint, String ipAddress) {

        // 1️⃣ Simple rule-based baseline (always works even if AI fails)
        ThreatAnalysisResult baseline = ruleBasedAnalysis(username, endpoint, ipAddress);

        try {
            // 2️⃣ Ask OpenAI for a threat classification
            ThreatAnalysisResult aiResult = callOpenAI(username, endpoint, ipAddress);

            // 3️⃣ Combine baseline + AI (you can make smarter merge logic here)
            double finalScore = Math.max(baseline.getScore(), aiResult.getScore());
            String finalLabel = finalScore >= 0.7 ? aiResult.getLabel() : baseline.getLabel();
            String finalReason = baseline.getReason() + " | AI: " + aiResult.getReason();

            return new ThreatAnalysisResult(finalScore, finalLabel, finalReason);
        } catch (Exception ex) {
            // If OpenAI call fails, fall back to baseline
            System.out.println("⚠ OpenAI threat analysis failed: " + ex.getMessage());
            return baseline;
        }
    }

    private ThreatAnalysisResult ruleBasedAnalysis(String username, String endpoint, String ipAddress) {
        double score = 0.1;
        String label = "NORMAL";
        String reason = "Normal activity";

        if (endpoint.contains("/admin")) {
            score = 0.8;
            label = "SUSPICIOUS";
            reason = "User accessing admin endpoint";
        }

        if (endpoint.contains("/login") && username.equalsIgnoreCase("admin")) {
            score = 0.9;
            label = "BRUTE_FORCE_SUSPECT";
            reason = "Admin login attempt flagged by rules";
        }

        return new ThreatAnalysisResult(score, label, reason);
    }

    private ThreatAnalysisResult callOpenAI(String username, String endpoint, String ipAddress) throws Exception {

        String prompt = """
                You are an API security analyzer. 
                Classify the threat level of this request as one of: NORMAL, SUSPICIOUS, MALICIOUS.

                Data:
                - username: %s
                - endpoint: %s
                - ip: %s

                Respond in JSON with fields: level (NORMAL/SUSPICIOUS/MALICIOUS), score (0 to 1), reason (short text).
                """.formatted(username, endpoint, ipAddress);

        String body = """
                {
                  "model": "%s",
                  "messages": [
                    {
                      "role": "user",
                      "content": "%s"
                    }
                  ]
                }
                """.formatted(model, prompt.replace("\"", "\\\""));

        String response = openAiClient.post()
                .body(body)
                .retrieve()
                .body(String.class);

        JsonNode json = objectMapper.readTree(response);
        String content = json
                .path("choices").get(0)
                .path("message")
                .path("content")
                .asText();

        // very simple parse: assume AI returns small JSON as described
        JsonNode parsed = objectMapper.readTree(content);

        String level = parsed.path("level").asText("NORMAL");
        double score = parsed.path("score").asDouble(0.1);
        String reason = parsed.path("reason").asText("AI analysis");

        return new ThreatAnalysisResult(score, level, reason);
    }
}
