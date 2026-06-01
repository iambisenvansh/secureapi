# Advanced Threat Forensics - Technical Design Document

## 1. Architecture Overview

### System Components

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend (React)                          │
│  ┌──────────────┬──────────────┬──────────────┬────────────┐ │
│  │ Overview     │ Threat       │ Network      │ Attack     │ │
│  │ Dashboard    │ Intelligence │ Forensics    │ Pattern    │ │
│  └──────────────┴──────────────┴──────────────┴────────────┘ │
│  ┌──────────────┬──────────────┬──────────────┬────────────┐ │
│  │ Incident     │ User         │ System       │ Reports    │ │
│  │ Response     │ Activity     │ Health       │ Page       │ │
│  └──────────────┴──────────────┴──────────────┴────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Backend API Layer (Spring Boot)                 │
│  ┌──────────────────────────────────────────────────────────┐│
│  │ REST Controllers (Forensics, Incidents, Alerts, Reports) ││
│  └──────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Service Layer (Business Logic)                  │
│  ┌──────────────┬──────────────┬──────────────┬────────────┐ │
│  │ Threat       │ Geolocation  │ IP           │ Anomaly    │ │
│  │ Classification│ Service     │ Reputation   │ Detection  │ │
│  └──────────────┴──────────────┴──────────────┴────────────┘ │
│  ┌──────────────┬──────────────┬──────────────┬────────────┐ │
│  │ Incident     │ Alert        │ Audit        │ Report     │ │
│  │ Management   │ Management   │ Logging      │ Generation │ │
│  └──────────────┴──────────────┴──────────────┴────────────┘ │
│  ┌──────────────┬──────────────┐                             │
│  │ Port Scanning│ Data         │                             │
│  │ Detection    │ Retention    │                             │
│  └──────────────┴──────────────┘                             │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│              Data Access Layer (Repositories)                │
│  ┌──────────────┬──────────────┬──────────────┬────────────┐ │
│  │ RequestLog   │ Incident     │ Alert        │ AuditLog   │ │
│  │ Repository   │ Repository   │ Repository   │ Repository │ │
│  └──────────────┴──────────────┴──────────────┴────────────┘ │
│  ┌──────────────┬──────────────┬──────────────┐             │
│  │ IPReputation │ Geolocation  │ AnomalyBase  │             │
│  │ Repository   │ Repository   │ Repository   │             │
│  └──────────────┴──────────────┴──────────────┘             │
└─────────────────────────────────────────────────────────────┘
                            ↓
┌─────────────────────────────────────────────────────────────┐
│                  MongoDB Database                            │
│  Collections: request_logs, incidents, alerts, audit_logs,  │
│  ip_reputation, geolocation_cache, anomaly_baselines        │
└─────────────────────────────────────────────────────────────┘
```

## 2. Extended Data Models

### RequestLog (Extended)
```java
@Document(collection = "request_logs")
public class RequestLog {
    @Id private String id;
    private String username;
    private String endpoint;
    private String clientIp;
    private LocalDateTime timestamp;
    
    // Network Information
    private Integer sourcePort;           // 0-65535
    private Integer destinationPort;      // 0-65535
    private String protocol;              // HTTP, HTTPS, DNS, TCP, UDP
    
    // Threat Analysis
    private String attackType;            // SQL Injection, XSS, DDoS, etc.
    private Integer confidenceScore;      // 0-100
    private String severityLevel;         // Critical, High, Medium, Low
    private double threatScore;           // 0-1
    private String threatLabel;           // NORMAL, SUSPICIOUS, MALICIOUS
    private String reason;
    
    // Request Details
    private Long payloadSize;             // bytes
    private String contentType;           // application/json, etc.
    private Integer responseStatusCode;   // 100-599
    private Map<String, String> headers;  // User-Agent, Referer, etc.
    
    // Geolocation
    private GeolocationData geolocation;  // country, city, lat, lon
    
    // Audit
    private String incidentId;            // Link to incident if applicable
}

public class GeolocationData {
    private String country;
    private String city;
    private Double latitude;
    private Double longitude;
    private String isp;
}
```

### Incident
```java
@Document(collection = "incidents")
public class Incident {
    @Id private String id;
    private String title;
    private String description;
    private String severity;             // Critical, High, Medium, Low
    private String status;               // Open, In Progress, Resolved, Closed
    private String assignedUser;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime closedAt;
    private String closureReason;
    private List<String> linkedThreatIds;
    private List<RemediationAction> actions;
    private List<StatusHistory> statusHistory;
}

public class RemediationAction {
    private String id;
    private String description;
    private String status;               // Pending, In Progress, Completed
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}

public class StatusHistory {
    private String oldStatus;
    private String newStatus;
    private LocalDateTime timestamp;
    private String changedBy;
}
```

### Alert
```java
@Document(collection = "alerts")
public class Alert {
    @Id private String id;
    private String threatId;
    private String severity;             // Critical, High, Medium, Low
    private String status;               // New, Acknowledged, Investigating, Resolved, False Positive
    private LocalDateTime createdAt;
    private LocalDateTime acknowledgedAt;
    private String acknowledgedBy;
    private String sourceIp;
    private String attackType;
    private String incidentId;
    private List<String> investigationNotes;
    private String falsePositiveReason;
}
```

### AuditLog
```java
@Document(collection = "audit_logs")
public class AuditLog {
    @Id private String id;
    private String userId;
    private String action;               // create, read, update, delete, export
    private String dataType;             // incident, alert, threat, etc.
    private String dataId;
    private LocalDateTime timestamp;
    private String oldValue;
    private String newValue;
    private String ipAddress;
    private Boolean immutable = true;    // Cannot be modified
}
```

### IPReputation
```java
@Document(collection = "ip_reputation")
public class IPReputation {
    @Id private String ipAddress;
    private Integer reputationScore;     // 0-100
    private String classification;       // malicious, suspicious, clean
    private Integer threatCount;
    private LocalDateTime lastUpdated;
    private LocalDateTime expiresAt;     // 24-hour TTL
}
```

### AnomalyBaseline
```java
@Document(collection = "anomaly_baselines")
public class AnomalyBaseline {
    @Id private String userId;
    private LocalDateTime baselineStartDate;
    private List<String> typicalAccessTimes;
    private List<String> typicalLocations;
    private List<String> typicalEndpoints;
    private Double typicalRequestFrequency;
    private LocalDateTime lastUpdated;
}
```

## 3. Service Layer Architecture

### ThreatClassificationService
- Analyzes request patterns for attack types
- Detects: SQL Injection, XSS, DDoS, Brute Force, Port Scanning, Command Injection, Path Traversal
- Calculates confidence scores (0-100)
- Assigns severity levels (Critical, High, Medium, Low)
- Returns deterministic results for same input

### GeolocationService
- Maps IP addresses to geographic locations
- Caches results with 24-hour TTL
- Supports 100,000 entry cache with LRU eviction
- Returns: country, city, latitude, longitude, ISP
- Handles cache misses by fetching from external API

### IPReputationService
- Scores IP addresses (0-100)
- Classifies: malicious (>75), suspicious (50-75), clean (<50)
- Updates scores based on new threats
- Tracks threat history per IP
- Supports reputation decay over time

### AnomalyDetectionService
- Establishes user baselines after 7 days of data
- Detects deviations >2 standard deviations
- Flags: new locations, unusual times, rapid requests, sensitive endpoint access
- Escalates severity for multiple anomalies within 1 hour
- Updates baselines weekly

### IncidentManagementService
- Creates and tracks incidents
- Manages status transitions (Open → In Progress → Resolved → Closed)
- Links threats to incidents
- Tracks remediation actions
- Maintains status history
- Enforces closure reason requirement

### AlertManagementService
- Generates alerts for Critical/High severity threats
- Manages alert status (New, Acknowledged, Investigating, Resolved, False Positive)
- Tracks investigation notes
- Updates IP reputation for false positives
- Supports alert search and filtering

### AuditLoggingService
- Logs all administrative actions
- Records: user, timestamp, action, data type, old/new values
- Immutable append-only storage
- Supports 90-day retention
- Enables compliance auditing

### ReportGenerationService
- Generates PDF and CSV reports
- Supports date range and filter options
- Includes: threat summary, top attack types, top IPs, geographic distribution
- Supports scheduled report generation
- Logs all report generation in audit logs

### PortScanningDetectionService
- Analyzes requests targeting multiple ports
- 5-minute time window for detection
- Flags when 5+ unique ports targeted from single IP
- Assigns High severity
- Tracks port scanning patterns over time

### DataRetentionService
- Archives request logs >90 days to cold storage
- Deletes request logs >1 year
- Retains incidents for 2 years
- Retains audit logs for 90 days
- Verifies data integrity before/after archival

## 4. API Endpoints

```
GET  /forensics/threats/by-ip/{ipAddress}
GET  /forensics/threats/by-type/{attackType}
GET  /forensics/threats/by-port/{port}
GET  /forensics/geolocation/{ipAddress}
POST /forensics/incidents
PUT  /forensics/incidents/{incidentId}
GET  /forensics/incidents/{incidentId}
POST /forensics/alerts
PUT  /forensics/alerts/{alertId}
GET  /forensics/reports/generate
GET  /forensics/audit-logs
```

All endpoints require Admin role and return 403 Forbidden if unauthorized.

## 5. Frontend Pages (React Components)

1. **OverviewDashboard** - KPIs, threat trends, top threats
2. **ThreatIntelligence** - Attack type breakdown, IP analysis, protocol distribution
3. **NetworkForensics** - IP reputation map, geolocation visualization, port scanning
4. **AttackPatternAnalysis** - Timeline visualization, frequency analysis, attack vectors
5. **IncidentResponse** - Alert management, incident tracking, status updates
6. **UserActivity** - Login attempts, access patterns, anomalies
7. **SystemHealth** - API performance, response times, error rates
8. **Reports** - Report generation, scheduling, download

## 6. Database Indexes

```javascript
// request_logs
db.request_logs.createIndex({ clientIp: 1 })
db.request_logs.createIndex({ attackType: 1 })
db.request_logs.createIndex({ destinationPort: 1 })
db.request_logs.createIndex({ timestamp: -1 })
db.request_logs.createIndex({ severityLevel: 1 })

// incidents
db.incidents.createIndex({ status: 1 })
db.incidents.createIndex({ createdAt: -1 })
db.incidents.createIndex({ assignedUser: 1 })

// alerts
db.alerts.createIndex({ status: 1 })
db.alerts.createIndex({ createdAt: -1 })
db.alerts.createIndex({ sourceIp: 1 })

// audit_logs
db.audit_logs.createIndex({ userId: 1 })
db.audit_logs.createIndex({ timestamp: -1 })
db.audit_logs.createIndex({ action: 1 })

// ip_reputation
db.ip_reputation.createIndex({ reputationScore: 1 })
db.ip_reputation.createIndex({ expiresAt: 1 }, { expireAfterSeconds: 0 })

// anomaly_baselines
db.anomaly_baselines.createIndex({ userId: 1 })
```

## 7. Security Architecture

- **RBAC**: Super Admin > Admin > Analyst (read-only)
- **Audit Trail**: All actions logged with user, timestamp, changes
- **Data Encryption**: Exports encrypted at rest and in transit
- **Sensitive Field Redaction**: IP addresses, credentials, hostnames masked in exports
- **Access Control**: 403 Forbidden for unauthorized access
- **Immutable Logs**: Audit logs cannot be modified or deleted

## 8. Integration Points

1. **Request Processing** → Threat Classification → Alert Generation
2. **Threat Detection** → Geolocation Lookup → IP Reputation Update
3. **Anomaly Detection** → Alert Generation → Incident Creation
4. **Administrative Actions** → Audit Logging
5. **Data Lifecycle** → Retention Policies → Archival/Deletion
