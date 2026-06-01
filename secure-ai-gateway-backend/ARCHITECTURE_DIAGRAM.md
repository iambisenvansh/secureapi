# 🏗️ Complete Architecture Diagram

## **System Architecture**

```
┌─────────────────────────────────────────────────────────────────┐
│                     REACT FRONTEND (Port 3000)                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  • Login/Register Page                                   │  │
│  │  • Admin Dashboard                                       │  │
│  │  • Analytics Charts                                      │  │
│  │  • Request Logs Table                                    │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────┬─────────────────────────────────────────┘
                         │ HTTP Requests (Axios)
                         │ Authorization: Bearer <JWT_TOKEN>
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│              SPRING BOOT BACKEND (Port 8081)                    │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │  FILTER CHAIN (Request Interception)                     │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │ 1. RateLimitFilter                                 │  │  │
│  │  │    • Track requests per IP                         │  │  │
│  │  │    • Classify: NORMAL / SUSPICIOUS / MALICIOUS    │  │  │
│  │  │    • Block if > 50 requests                        │  │  │
│  │  │    • Log to ThreatStore                            │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │                        ▼                                   │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │ 2. JwtAuthenticationFilter                         │  │  │
│  │  │  