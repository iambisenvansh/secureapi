# 🔐 Secure AI Gateway

> **AI-Powered API Threat Monitoring & Security Gateway**

Secure AI Gateway is a full-stack security platform built with **Spring Boot, Spring Security, JWT, MongoDB, React, and OpenAI-based threat analysis**. It monitors API traffic, applies rate limiting and authentication, classifies requests by threat level, stores security logs, and provides an analytics dashboard for administrators.

---

## 📌 Project Overview

Modern web applications expose APIs that can be targeted by:

- Brute-force attempts
- Abnormal request patterns
- Unauthorized access
- API abuse
- Suspicious administrative access
- Malicious traffic

Traditional rule-based security checks are fast but can be limited when dealing with more complex patterns. This project uses a **hybrid security approach**:

```text
Incoming API Request
        │
        ▼
┌───────────────────────┐
│   Rate Limit Filter   │
└───────────┬───────────┘
            ▼
┌───────────────────────┐
│ JWT Authentication    │
└───────────┬───────────┘
            ▼
┌───────────────────────┐
│ Threat Detection      │
│ Rule-Based + AI       │
└───────────┬───────────┘
            ▼
┌───────────────────────┐
│ MongoDB Request Logs  │
└───────────┬───────────┘
            ▼
┌───────────────────────┐
│ React Security        │
│ Dashboard              │
└───────────────────────┘
```

---

## ✨ Key Features

### 🛡️ 1. JWT Authentication

- User registration
- Username/password authentication
- JWT token generation
- Protected API routes
- BCrypt password hashing
- Authentication context on the React frontend

### 🚦 2. Rate Limiting

The gateway tracks requests by client IP address.

Current thresholds:

| Requests | Classification |
|---:|---|
| 0–20 | NORMAL |
| 21–50 | SUSPICIOUS |
| 51+ | BLOCKED / MALICIOUS |

Blocked IP addresses are maintained in memory and subsequent requests are rejected.

### 🤖 3. Hybrid Threat Detection

The backend combines:

**Rule-based analysis**
- Fast baseline detection
- Detects suspicious administrative access
- Flags potentially suspicious admin login activity

**AI-based analysis**
- Sends request context to the configured OpenAI model
- Classifies requests as `NORMAL`, `SUSPICIOUS`, or `MALICIOUS`
- Produces a confidence score and reason

If AI analysis fails, the application falls back to the rule-based result.

### 📊 4. Security Analytics Dashboard

The React dashboard provides:

- Total request count
- Normal requests
- Suspicious requests
- Malicious requests
- Recent request logs
- Endpoint-based analytics
- Threat charts
- Protected routes

### 📝 5. Request & Threat Logging

The system records security-related request information such as:

- Username
- Endpoint
- Client IP
- Timestamp
- Threat score
- Threat label
- Detection reason

### 🧪 6. Test Data Generation

The backend includes endpoints for generating sample traffic and threat data, making it easier to demonstrate the dashboard without manually creating requests.

---

## 🏗️ Technology Stack

### Backend

- **Java 17**
- **Spring Boot 3.5.8**
- Spring Web
- Spring Security
- Spring Data MongoDB
- Spring Data Redis
- JWT (`jjwt`)
- BCrypt
- Maven
- Lombok

### Frontend

- **React 19**
- **Vite**
- React Router
- Axios
- Chart.js
- Recharts
- Tailwind CSS

### Database

- **MongoDB**

### AI

- **OpenAI API**
- Configurable OpenAI model

---

## 📂 Project Structure

```text
secureapi-main/
│
├── secure-ai-gateway-backend/
│   │
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/vansh/secure_ai_gateway_backend/
│   │   │   │       ├── config/
│   │   │   │       ├── controller/
│   │   │   │       ├── dto/
│   │   │   │       ├── model/
│   │   │   │       ├── repository/
│   │   │   │       ├── security/
│   │   │   │       └── service/
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │
│   │   └── test/
│   │
│   ├── pom.xml
│   │
│   ├── package.json
│   ├── src/
│   │   ├── api/
│   │   ├── components/
│   │   ├── context/
│   │   ├── pages/
│   │   ├── routes/
│   │   └── App.jsx
│   │
│   └── vite.config.js
│
└── README.md
```

> The submitted archive currently contains the backend and React application under the `secure-ai-gateway-backend` project directory.

---

## 🔄 How the System Works

### 1. Authentication

A user registers and logs in:

```text
Username + Password
        ↓
Spring Security Authentication
        ↓
BCrypt password verification
        ↓
JWT generated
        ↓
Frontend stores token
```

The JWT is then sent with protected API requests.

### 2. API Request

When an API request reaches the backend:

```text
Client
  ↓
RateLimitFilter
  ↓
JwtAuthenticationFilter
  ↓
Request/Threat Analysis
  ↓
Controller
  ↓
MongoDB
  ↓
JSON Response
```

### 3. Threat Analysis

The threat detection service first performs a rule-based analysis.

For example:

```text
/api/admin/...
      ↓
Suspicious administrative access
      ↓
Higher threat score
```

The service can then request an additional analysis from OpenAI.

The final result combines the baseline and AI scores:

```text
finalScore = max(ruleScore, aiScore)
```

If the AI service is unavailable, the rule-based result is returned instead.

---

## 🔌 Important API Endpoints

### Authentication

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/auth/register` | Register a user |
| POST | `/auth/login` | Authenticate and receive JWT |
| GET | `/auth/profile` | Access protected profile route |

### Analytics

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/admin/analytics/summary` | Threat summary |
| GET | `/admin/analytics/recent` | Recent request logs |
| GET | `/admin/analytics/by-endpoint` | Endpoint statistics |

### Test Requests

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/test/normal` | Generate a normal request |
| GET | `/api/test/suspicious` | Generate a suspicious request |
| GET | `/api/test/malicious` | Generate a malicious request |

### Test Data

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/seed/generate-data` | Generate sample request logs |
| DELETE | `/api/seed/clear-data` | Clear request logs |
| GET | `/api/seed/stats` | View stored statistics |

### Health

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/` | Application root |
| GET | `/health` | Health check |

---

## ⚙️ Prerequisites

Install the following before running the project:

- **Java 17+**
- **Maven**
- **Node.js + npm**
- **MongoDB**
- Optional: an OpenAI API key for AI-based threat analysis

Verify installations:

```bash
java -version
mvn -version
node -v
npm -v
mongosh --version
```

---

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <YOUR_GITHUB_REPOSITORY_URL>
cd secureapi-main
```

Replace `<YOUR_GITHUB_REPOSITORY_URL>` with your actual GitHub repository URL.

---

### 2. Start MongoDB

Make sure MongoDB is running locally.

The current application configuration uses:

```text
mongodb://localhost:27017/secure-gateway
```

---

### 3. Configure Backend

Open:

```text
secure-ai-gateway-backend/src/main/resources/application.properties
```

The backend runs on:

```text
http://localhost:8081
```

For AI threat analysis, configure your OpenAI credentials using environment variables rather than committing secrets to Git.

Example:

```bash
OPENAI_API_KEY=your_api_key_here
```

**Never commit a real API key or JWT secret to GitHub.**

---

### 4. Start Backend

```bash
cd secure-ai-gateway-backend
mvn spring-boot:run
```

Backend:

```text
http://localhost:8081
```

---

### 5. Install Frontend Dependencies

From the frontend project directory:

```bash
npm install
```

Then start Vite:

```bash
npm run dev
```

The frontend is normally available at:

```text
http://localhost:5173
```

---

## 🧪 Demonstrating the Project

### Step 1 — Register

Send:

```http
POST /auth/register
```

Example request:

```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}
```

### Step 2 — Login

Send:

```http
POST /auth/login
```

The response contains a JWT token.

### Step 3 — Use the JWT

Include the token in protected requests:

```http
Authorization: Bearer <JWT_TOKEN>
```

### Step 4 — Generate Threat Data

You can generate sample data:

```http
POST /api/seed/generate-data?count=50
```

### Step 5 — Open Dashboard

Open:

```text
http://localhost:5173
```

Review the threat statistics, charts, logs, and analytics.

---

## 🔐 Security Design

The project follows a layered security approach:

```text
                    Internet
                       │
                       ▼
             ┌──────────────────┐
             │   API Gateway     │
             └────────┬─────────┘
                      │
          ┌───────────▼───────────┐
          │    Rate Limiting      │
          └───────────┬───────────┘
                      │
          ┌───────────▼───────────┐
          │ JWT Authentication     │
          └───────────┬───────────┘
                      │
          ┌───────────▼───────────┐
          │ Threat Detection       │
          │ Rules + AI             │
          └───────────┬───────────┘
                      │
          ┌───────────▼───────────┐
          │ Spring Controllers     │
          └───────────┬───────────┘
                      │
          ┌───────────▼───────────┐
          │ MongoDB Request Logs   │
          └───────────────────────┘
```

### Security Components

- JWT-based authentication
- BCrypt password hashing
- Protected routes
- IP-based rate limiting
- Blocked IP tracking
- Threat classification
- Security event logging
- Admin analytics
- AI-assisted analysis
- Fallback to rule-based detection

---

## 📈 Threat Classification

The application uses three primary threat labels:

| Label | Meaning |
|---|---|
| `NORMAL` | Request appears normal |
| `SUSPICIOUS` | Request shows potentially abnormal behavior |
| `MALICIOUS` | Request is considered potentially harmful |

Threat scores are represented between `0` and `1`.

Example:

```text
0.12 → NORMAL
0.62 → SUSPICIOUS
0.91 → MALICIOUS
```

---

## 🧩 Main Backend Components

### Controllers

- `AuthController`
- `AdminAnalyticsController`
- `RequestLogController`
- `DataSeederController`
- `HealthController`

### Security

- `JwtAuthenticationFilter`
- `JwtUtil`
- `RateLimitFilter`
- `CustomUserDetailsService`
- `CustomUserDetails`
- `SecurityConfig`

### Services

- `ThreatDetectionService`
- `ThreatAnalysisService`
- `ThreatStore`

### Models

- `User`
- `RequestLog`
- `ThreatEvent`
- `ThreatAnalysisResult`

### Repositories

- `UserRepository`
- `RequestLogRepository`

---

## 🖥️ Frontend Pages

The React application includes pages/components for:

- Login
- Dashboard
- Threats
- Logs
- Analytics
- Blocked requests
- Protected routes
- Navigation/sidebar
- Threat charts
- Statistics cards

API communication is handled through Axios-based API modules.

---

## 📊 Dashboard

The dashboard is designed to provide security visibility through:

```text
┌─────────────────────────────────────────┐
│             SECURITY DASHBOARD           │
├────────────┬────────────┬───────────────┤
│ Total      │ Suspicious │ Malicious     │
│ Requests   │ Requests   │ Requests      │
├────────────┴────────────┴───────────────┤
│                                         │
│       Threat Distribution Charts        │
│                                         │
├─────────────────────────────────────────┤
│             Recent Request Logs         │
└─────────────────────────────────────────┘
```

---

## 🧪 Testing

The project includes a Spring Boot test structure.

Run backend tests with:

```bash
mvn test
```

Build the backend with:

```bash
mvn clean package
```

Build the frontend with:

```bash
npm run build
```

---

## ⚠️ Important Security Notes

This repository is intended as an internship/project demonstration.

Before production deployment:

1. Replace the demo JWT secret.
2. Store secrets in environment variables or a secret manager.
3. Never commit API keys.
4. Use HTTPS.
5. Move rate-limit state to a distributed store such as Redis for multi-instance deployments.
6. Add proper request-window expiration to rate-limit counters.
7. Add comprehensive unit and integration tests.
8. Add centralized monitoring and alerting.
9. Apply least-privilege database credentials.
10. Validate and sanitize all externally supplied data.

---

## 🔮 Future Improvements

Potential production-level improvements include:

- Redis-backed distributed rate limiting
- API gateway deployment using Spring Cloud Gateway
- Docker containerization
- Kubernetes deployment
- Centralized logging
- Prometheus/Grafana monitoring
- Advanced anomaly detection
- Persistent blocked-IP management
- Role-based access control improvements
- Security alert notifications
- Automated incident response
- SIEM integration
- More comprehensive attack signatures
- Automated CI/CD security scanning

---

## 🎯 Project Objectives

The main objectives are to:

- Secure REST APIs against common abuse patterns
- Authenticate users securely
- Detect suspicious API behavior
- Apply request rate limiting
- Record security events
- Provide real-time security visibility
- Demonstrate AI-assisted threat analysis
- Build a scalable foundation for API security monitoring

---

## 👨‍💻 Author

**Vansh Bisen**

B.Tech — Cyber Security

GitHub: **Add your GitHub profile/repository link**

---

## 📄 License

This project is intended for educational, internship, and demonstration purposes.
