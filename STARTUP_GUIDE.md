# 🚀 Secure AI Gateway - Complete Startup Guide

## **Prerequisites**
- ✅ Java 21 installed
- ✅ MongoDB running on `localhost:27017`
- ✅ IntelliJ IDEA Community Edition 2025.2.2
- ✅ Maven installed

---

## **STEP 1: Start MongoDB**

### **Option A: Using Command Line**
```bash
# Create data directory (already done)
# C:\data\db

# Start MongoDB
mongod --dbpath C:\data\db
```

**Expected Output:**
```
{"t":{"$date":"2026-04-06T12:38:50.100+05:30"},"s":"I","c":"CONTROL","id":5945603,"ctx":"thread1","msg":"Multi threading initialized"}
```

### **Option B: Using MongoDB Compass (GUI)**
1. Open MongoDB Compass
2. Connect to `mongodb://localhost:27017`
3. Create database: `secure-gateway`

---

## **STEP 2: Configure IntelliJ IDEA**

### **2.1 Open Project**
1. File → Open → Select `secure-ai-gateway-backend` folder
2. Wait for Maven to download dependencies

### **2.2 Configure Run Configuration**
1. Click **Run** → **Edit Configurations**
2. Click **+** → **Spring Boot**
3. Set:
   - **Name:** `SecureAiGateway`
   - **Main class:** `com.vansh.secure_ai_gateway_backend.SecureAiGatewayBackendApplication`
   - **VM options:** `-Dspring.profiles.active=dev`
   - **Working directory:** `$PROJECT_DIR$`

### **2.3 Build Project**
1. Click **Build** → **Build Project** (or Ctrl+F9)
2. Wait for compilation to complete

---

## **STEP 3: Run Backend**

### **Option A: Using IntelliJ Run Button**
1. Click the **Run** button (green play icon) in top-right
2. Or press **Shift+F10**

### **Option B: Using Maven Command**
```bash
cd secure-ai-gateway-backend
mvn clean spring-boot:run
```

### **Expected Output:**
```
2026-04-06T12:13:09.490+05:30  INFO 12900 --- [secure-ai-gateway-backend] [  restartedMain] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8081 (http) with context path '/'
2026-04-06T12:13:09.500+05:30  INFO 12900 --- [secure-ai-gateway-backend] [  restartedMain] c.v.s.SecureAiGatewayBackendApplication  : Started SecureAiGatewayBackendApplication in 7.352 seconds
```

---

## **STEP 4: Test Backend**

### **Test 1: Health Check**
```bash
curl http://localhost:8081/
```

**Expected Response:**
```json
{
  "message": "🚀 Secure AI Gateway Backend is running!",
  "timestamp": "2026-04-06T12:13:09.500+05:30",
  "status": "UP",
  "version": "1.0.0"
}
```

### **Test 2: Register User**
```bash
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

**Expected Response:**
```
User registered successfully
```

### **Test 3: Login**
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcxNzY2NzgwMCwiZXhwIjoxNzE3NjcxNDAwfQ.signature"
}
```

### **Test 4: Access Protected Endpoint**
```bash
curl -X GET http://localhost:8081/admin/analytics/summary \
  -H "Authorization: Bearer <YOUR_TOKEN_HERE>"
```

**Expected Response:**
```json
{
  "total": 0,
  "normal": 0,
  "suspicious": 0,
  "malicious": 0
}
```

---

## **TROUBLESHOOTING**

### **❌ Error: Port 8081 already in use**
```bash
# Find process using port 8081
netstat -ano | findstr :8081

# Kill process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

### **❌ Error: MongoDB connection refused**
```bash
# Check if MongoDB is running
mongod --version

# Start MongoDB
mongod --dbpath C:\data\db
```

### **❌ Error: 403 Forbidden**
- Make sure you're using a valid JWT token
- Token format: `Authorization: Bearer <token>`
- Check token expiry (1 hour)

### **❌ Error: Cannot find main class**
1. Right-click project → **Maven** → **Reload Projects**
2. Click **Build** → **Rebuild Project**

---

## **QUICK REFERENCE**

| Component | Port | Status |
|-----------|------|--------|
| Backend | 8081 | ✅ Running |
| MongoDB | 27017 | ✅ Running |
| Frontend | 5173 | ⏳ Not started |

---

## **API ENDPOINTS**

### **Public Endpoints (No Auth Required)**
- `GET /` - Health check
- `GET /health` - Detailed health
- `POST /auth/register` - Register user
- `POST /auth/login` - Login user
- `GET /api/test/normal` - Test normal request
- `GET /api/test/suspicious` - Test suspicious request
- `GET /api/test/malicious` - Test malicious request

### **Protected Endpoints (JWT Required)**
- `GET /auth/profile` - Get user profile
- `GET /admin/analytics/summary` - Get analytics summary
- `GET /admin/analytics/recent` - Get recent logs
- `GET /admin/analytics/by-endpoint` - Get endpoint stats

---

## **NEXT STEPS**

1. ✅ Backend running on port 8081
2. ⏳ Start MongoDB
3. ⏳ Test API endpoints
4. ⏳ Start React frontend on port 5173
5. ⏳ Connect frontend to backend

---

**Need Help?** Check the logs in IntelliJ console for detailed error messages.
