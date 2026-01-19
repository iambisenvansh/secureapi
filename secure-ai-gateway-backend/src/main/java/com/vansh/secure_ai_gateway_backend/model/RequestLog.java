package com.vansh.secure_ai_gateway_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "request_logs")
public class RequestLog {

    @Id
    private String id;

    private String username;
    private String endpoint;

    // MUST match repository method name
    private String clientIp;

    private LocalDateTime timestamp;

    // Threat-related fields
    private double threatScore;   // 0–1
    private String threatLabel;   // NORMAL / SUSPICIOUS / MALICIOUS
    private String reason;        // explanation

    public RequestLog() {}

    public RequestLog(
            String username,
            String endpoint,
            String clientIp,
            LocalDateTime timestamp,
            double threatScore,
            String threatLabel,
            String reason
    ) {
        this.username = username;
        this.endpoint = endpoint;
        this.clientIp = clientIp;
        this.timestamp = timestamp;
        this.threatScore = threatScore;
        this.threatLabel = threatLabel;
        this.reason = reason;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }

    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public double getThreatScore() { return threatScore; }
    public void setThreatScore(double threatScore) { this.threatScore = threatScore; }

    public String getThreatLabel() { return threatLabel; }
    public void setThreatLabel(String threatLabel) { this.threatLabel = threatLabel; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
