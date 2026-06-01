# ⚡ Quick Reference Card

## **Start Backend**

```powershell
cd secure-ai-gateway-backend
.\run-backend.ps1
```

## **Test Endpoints**

### **1. Register User**
```bash
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","email":"user1@test.com","password":"pass123"}'
```

### **2. Login**
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123"}'
```

**Response:**
```json
{"token":"eyJhbGciOiJIUzI1NiJ9..."}
```

### **3. Use Token for Protected Endpoint**
```bash
curl -X GET http://localhost:8081/admin/analytics/summary \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## **Port Configuration**

| Service | Port | URL |
|---------|------|-----|
| Backend | 8081 | http://localhost:8081 |
| MongoDB | 27017 | mongodb://localhost:27017 |
| Frontend | 3000 | http://localhost:3000 |

## **Common Issues**

| Issue | Fix |
|-------|-----|
| Port in use | `Get-NetTCPConnection -LocalPort 8081 \| Stop-Process -Force` |
| MongoDB down | `mongod` |
| Build fails | `mvn clean compile` |
| 403 Forbidden | Add JWT token to Authorization header |

## **File Locations**

- Config: `src/main/resources/application.properties`
- Controllers: `src/main/java/.../controller/`
- Security: `src/main/java/.../security/`
- Models: `src/main/java/.../model/`

## **Key Endpoints**

| Method | Endpoint | Auth | Purpose |
|--------|----------|------|---------|
| GET | `/` | No | Health check |
| POST | `/auth/register` | No | Register user |
| POST | `/auth/login` | No | Get JWT token |
| GET | `/admin/analytics/summary` | Yes | Get threat stats |
| GET | `/api/test/normal` | No | Test normal request |
| GET | `/api/test/suspicious` | No | Test suspicious request |

## **JWT Token Structure**

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMSIsImlhdCI6MTcwOTU2NzgwMCwiZXhwIjoxNzA5NTcxNDAwfQ.signature

Header.Payload.Signature
```

**Payload contains:**
- `sub`: username
- `iat`: issued at time
- `exp`: expiry time (1 hour)

## **Rate Limiting**

- **Normal**: 0-19 requests
- **Suspicious**: 20-50 requests
- **Malicious**: 51+ requests (BLOCKED)

## **MongoDB Collections**

```javascript
// users collection
{
  "_id": ObjectId,
  "username": "user1",
  "email": "user1@test.com",
  "password": "$2a$10$hashed...",
  "roles": ["ROLE_ADMIN"]
}

// request_logs collection
{
  "_id": ObjectId,
  "username": "user1",
  "endpoint": "/api/test/normal",
  "clientIp": "127.0.0.1",
  "timestamp": ISODate,
  "threatScore": 0.12,
  "threatLabel": "NORMAL",
  "reason": "Normal activity"
}
```

## **Environment Variables**

```bash
# Set OpenAI API key
$env:OPENAI_API_KEY = "sk-proj-xxxxx"

# Or in application.properties
openai.api.key=sk-proj-xxxxx
```

## **Useful Maven Commands**

```bash
# Clean build
mvn clean

# Compile only
mvn compile

# Run tests
mvn test

# Build JAR
mvn package

# Run application
mvn spring-boot:run

# Check dependencies
mvn dependency:tree
```

## **Debug Mode**

```bash
# Run with debug logging
mvn spring-boot:run -Dspring-boot.run.arguments="--debug"

# Or set in application.properties
logging.level.root=DEBUG
logging.level.org.springframework.security=DEBUG
```

---

**Last Updated:** April 6, 2026  
**Backend Version:** 1.0.0  
**Java Version:** 21  
**Spring Boot Version:** 3.5.8
