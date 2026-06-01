# 🚀 Secure AI Gateway Backend - Setup & Troubleshooting Guide

## **Prerequisites**

- Java 21 or higher
- Maven 3.8+
- MongoDB (running on localhost:27017)
- OpenAI API Key

---

## **Quick Start**

### **Option 1: Using PowerShell Script (Recommended)**

```powershell
# Navigate to backend directory
cd secure-ai-gateway-backend

# Run the startup script
.\run-backend.ps1
```

### **Option 2: Using Batch Script**

```cmd
cd secure-ai-gateway-backend
run-backend.bat
```

### **Option 3: Manual Maven Commands**

```bash
cd secure-ai-gateway-backend

# Clean build
mvn clean

# Build project
mvn package -DskipTests

# Run application
mvn spring-boot:run
```

---

## **Verify Application is Running**

Once started, you should see:
```
Tomcat started on port 8081 (http) with context path '/'
Started SecureAiGatewayBackendApplication in X.XXX seconds
```

Test with:
```bash
curl http://localhost:8081/
```

Expected response:
```json
{
  "message": "🚀 Secure AI Gateway Backend is running!",
  "status": "UP",
  "version": "1.0.0",
  "timestamp": "2026-04-06T12:30:00"
}
```

---

## **API Endpoints**

### **Public Endpoints (No Authentication Required)**

```bash
# Health check
GET http://localhost:8081/

# Register new user
POST http://localhost:8081/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123"
}

# Login
POST http://localhost:8081/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}

# Response:
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### **Protected Endpoints (Requires JWT Token)**

```bash
# Get analytics summary
GET http://localhost:8081/admin/analytics/summary
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

# Get recent logs
GET http://localhost:8081/admin/analytics/recent
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

# Get endpoint analytics
GET http://localhost:8081/admin/analytics/by-endpoint
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

### **Test Endpoints (Public)**

```bash
# Normal request
GET http://localhost:8081/api/test/normal

# Suspicious request
GET http://localhost:8081/api/test/suspicious

# Malicious request
GET http://localhost:8081/api/test/malicious
```

---

## **Troubleshooting**

### **Issue 1: Port 8080/8081 Already in Use**

**Error:**
```
Web server failed to start. Port 8080 was already in use.
```

**Solution:**

PowerShell:
```powershell
# Kill process on port 8080
Get-NetTCPConnection -LocalPort 8080 | Stop-Process -Force

# Kill process on port 8081
Get-NetTCPConnection -LocalPort 8081 | Stop-Process -Force
```

CMD:
```cmd
netstat -ano | findstr ":8080"
taskkill /PID <PID> /F
```

---

### **Issue 2: MongoDB Connection Failed**

**Error:**
```
Failed to connect to MongoDB at localhost:27017
```

**Solution:**

1. Check if MongoDB is running:
```powershell
Get-Process mongod
```

2. Start MongoDB:
```bash
mongod
```

3. Verify connection string in `application.properties`:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/secure-gateway
```

---

### **Issue 3: 403 Forbidden Error**

**Error:**
```
403 Forbidden when accessing /admin/analytics/summary
```

**Solution:**

1. Make sure you have a valid JWT token from login
2. Include token in Authorization header:
```bash
Authorization: Bearer <your_token_here>
```

3. Token format must be: `Bearer eyJhbGc...`

---

### **Issue 4: OpenAI API Errors**

**Error:**
```
OpenAI threat analysis failed: Invalid API key
```

**Solution:**

1. Check your OpenAI API key in `application.properties`:
```properties
openai.api.key=sk-proj-xxxxx
```

2. Verify the key is valid at https://platform.openai.com/api-keys

3. Make sure the model is available:
```properties
openai.model=gpt-4.1-mini
```

---

### **Issue 5: Application Shuts Down Immediately**

**Error:**
```
Process finished with exit code 130
```

**Solution:**

1. Check for compilation errors:
```bash
mvn clean compile
```

2. Check logs for specific errors

3. Verify all dependencies are installed:
```bash
mvn dependency:resolve
```

---

## **Configuration Files**

### **application.properties**

```properties
# Server
server.port=8081

# MongoDB
spring.data.mongodb.uri=mongodb://localhost:27017/secure-gateway

# JWT
security.jwt.secret=THIS_IS_A_DEMO_SECRET_KEY_CHANGE_IT_1234567890
security.jwt.expiration-ms=3600000

# OpenAI
openai.api.url=https://api.openai.com/v1/chat/completions
openai.model=gpt-4.1-mini
openai.api.key=${OPENAI_API_KEY}
```

---

## **Project Structure**

```
secure-ai-gateway-backend/
├── src/main/java/com/vansh/secure_ai_gateway_backend/
│   ├── config/              # Configuration classes
│   ├── controller/          # REST API endpoints
│   ├── security/            # Security filters & JWT
│   ├── service/             # Business logic
│   ├── repository/          # MongoDB data access
│   ├── dto/                 # Request/Response objects
│   └── model/               # Database entities
├── src/main/resources/
│   └── application.properties
├── pom.xml                  # Maven dependencies
├── run-backend.ps1          # PowerShell startup script
└── run-backend.bat          # Batch startup script
```

---

## **Key Features**

✅ JWT Authentication  
✅ Rate Limiting (50 requests per IP)  
✅ AI-Powered Threat Detection  
✅ Request Logging to MongoDB  
✅ Admin Analytics Dashboard  
✅ Role-Based Access Control  
✅ CORS Configuration  

---

## **Next Steps**

1. ✅ Backend is running on `http://localhost:8081`
2. 🔄 Start the React frontend
3. 📊 Access the admin dashboard
4. 🔐 Test authentication flow

---

## **Support**

For issues, check:
- Application logs in IDE console
- MongoDB connection status
- OpenAI API key validity
- Port availability

