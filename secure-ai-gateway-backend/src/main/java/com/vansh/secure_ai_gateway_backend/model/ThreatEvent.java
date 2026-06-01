package com.vansh.secure_ai_gateway_backend.model;

import java.time.LocalDateTime;

public class ThreatEvent {

    private String ip;
    private String endpoint;
    private String method;
    private String label;
    private LocalDateTime timestamp;

    public ThreatEvent(String ip, String endpoint, String method, String label, LocalDateTime timestamp) {
        this.ip = ip;
        this.endpoint = endpoint;
        this.method = method;
        this.label = label;
        this.timestamp = timestamp;
    }

    public String getIp() { return ip; }
    public String getEndpoint() { return endpoint; }
    public String getMethod() { return method; }
    public String getLabel() { return label; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
