@echo off
REM Secure AI Gateway Backend Startup Script

echo.
echo ========================================
echo Secure AI Gateway Backend
echo ========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/
    pause
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21 or higher
    pause
    exit /b 1
)

echo [✓] Java and Maven found
echo.

REM Kill any process on port 8080 and 8081
echo Cleaning up ports...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8080"') do taskkill /PID %%a /F 2>nul
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8081"') do taskkill /PID %%a /F 2>nul

echo [✓] Ports cleaned
echo.

REM Clean and build
echo Building project...
call mvn clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)

echo [✓] Build successful
echo.

REM Run the application
echo Starting Spring Boot application on port 8081...
echo.
call mvn spring-boot:run

pause
