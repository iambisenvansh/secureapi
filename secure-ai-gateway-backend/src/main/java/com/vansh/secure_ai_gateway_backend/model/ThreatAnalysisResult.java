package com.vansh.secure_ai_gateway_backend.model;

public class ThreatAnalysisResult {

    private double score;       // 0–1
    private String label;       // NORMAL / SUSPICIOUS / MALICIOUS
    private String reason;
    private String rawModelLabel;

    public ThreatAnalysisResult() {}
    public String getRawModelLabel() { return rawModelLabel; }
    public void setRawModelLabel(String rawModelLabel) { this.rawModelLabel = rawModelLabel; }

    public ThreatAnalysisResult(double score, String label, String reason) {
        this.score = score;
        this.label = label;
        this.reason = reason;
    }

    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
