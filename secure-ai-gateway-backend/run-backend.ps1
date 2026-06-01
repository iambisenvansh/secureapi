# Secure AI Gateway Backend Startup Script (PowerShell)

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Secure AI Gateway Backend" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Maven is installed
$mvnCheck = Get-Command mvn -ErrorAction SilentlyContinue
if (-not $mvnCheck) {
    Write-Host "ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Maven from https://maven.apache.org/" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Java is installed
$javaCheck = Get-Command java -ErrorAction SilentlyContinue
if (-not $javaCheck) {
    Write-Host "ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 21 or higher" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[✓] Java and Maven found" -ForegroundColor Green
Write-Host ""

# Kill any process on port 8080 and 8081
Write-Host "Cleaning up ports..." -ForegroundColor Yellow
Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue }
Get-NetTCPConnection -LocalPort 8081 -ErrorAction SilentlyContinue | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force -ErrorAction SilentlyContinue }

Write-Host "[✓] Ports cleaned" -ForegroundColor Green
Write-Host ""

# Clean and build
Write-Host "Building project..." -ForegroundColor Yellow
mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Build failed" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[✓] Build successful" -ForegroundColor Green
Write-Host ""

# Run the application
Write-Host "Starting Spring Boot application on port 8081..." -ForegroundColor Green
Write-Host ""
mvn spring-boot:run

Read-Host "Press Enter to exit"
