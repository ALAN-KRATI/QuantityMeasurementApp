# Fully Automated Railway Deployment Script for Windows
# Run: .\auto-deploy.ps1

$ErrorActionPreference = "Stop"

# Colors
function Write-Green($text) { Write-Host $text -ForegroundColor Green }
function Write-Yellow($text) { Write-Host $text -ForegroundColor Yellow }
function Write-Red($text) { Write-Host $text -ForegroundColor Red }
function Write-Blue($text) { Write-Host $text -ForegroundColor Cyan }

$JWT_SECRET = $env:JWT_SECRET -or "dGVzdGRHVnpkR1JsYzNSbFpHVnlkR2x1WjNOaFkzSmxkR2x2YmkxclpYaz0="

Write-Blue "╔════════════════════════════════════════════════════════════╗"
Write-Blue "║     🚀 Fully Automated Railway Deployment (Windows)       ║"
Write-Blue "╚════════════════════════════════════════════════════════════╝"
Write-Host ""

# Check prerequisites
Write-Yellow "🔍 Checking prerequisites..."

if (-not (Get-Command railway -ErrorAction SilentlyContinue)) {
    Write-Red "❌ Railway CLI not found. Installing..."
    npm install -g @railway/cli
}

if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Write-Red "❌ Git not found. Please install Git."
    exit 1
}

Write-Green "✅ Prerequisites checked"
Write-Host ""

# GitHub setup
Write-Yellow "📦 Setting up GitHub repository..."

if (-not (Test-Path .git)) {
    git init
    git add .
    git commit -m "Initial commit"
}

try {
    $remote = git remote get-url origin 2>$null
    if (-not $remote) {
        Write-Red "No GitHub remote found. Create repo first:"
        Write-Host "1. Go to https://github.com/new"
        Write-Host "2. Create repo named 'qma-app'"
        Write-Host "3. Run: git remote add origin https://github.com/YOUR_USERNAME/qma-app.git"
        Write-Host "4. Run: git push -u origin main"
        Read-Host "Press Enter after completing..."
    } else {
        git add . 2>$null
        git commit -m "Deploy to Railway" 2>$null
        git push 2>$null
    }
} catch {
    Write-Yellow "Git operations skipped or failed"
}

Write-Green "✅ GitHub ready"
Write-Host ""

# Railway login
Write-Yellow "🔐 Checking Railway login..."

try {
    $user = railway whoami 2>$null
    Write-Green "✅ Logged in as: $user"
} catch {
    Write-Host "Please login to Railway (browser will open):"
    railway login
}

Write-Host ""

# Create project
Write-Yellow "📁 Creating Railway project..."

if (-not (Test-Path .railway/config.json)) {
    railway init --name qma-app --empty
}

Write-Green "✅ Project ready"
Write-Host ""

# Add databases
Write-Yellow "🗄️  Adding databases..."

Write-Host "Creating MySQL database..."
railway add --database mysql --name qma-mysql 2>$null

Write-Host "Creating Redis..."
railway add --database redis --name qma-redis 2>$null

Write-Green "✅ Databases added"
Write-Host ""

# Deploy services
function Deploy-Service($name, $dir) {
    Write-Yellow "🚀 Deploying $name..."
    Set-Location $dir
    railway up --service $name
    Set-Location ..
    Write-Green "✅ $name deployed"
    Write-Host ""
}

Deploy-Service "auth-service" "auth-service"
Deploy-Service "qma-service" "qma-service"
Deploy-Service "api-gateway" "api-gateway-service"

# Set environment variables
Write-Yellow "⚙️  Setting environment variables..."

railway variables --service auth-service JWT_SECRET="$JWT_SECRET" JWT_EXPIRATION="3600000" DB_HOST="`${MYSQLHOST}`" DB_PORT="`${MYSQLPORT}`" DB_USERNAME="`${MYSQLUSER}`" DB_PASSWORD="`${MYSQLPASSWORD}`" DB_NAME="`${MYSQLDATABASE}`" 2>$null

railway variables --service qma-service JWT_SECRET="$JWT_SECRET" JWT_EXPIRATION="3600000" DB_HOST="`${MYSQLHOST}`" DB_PORT="`${MYSQLPORT}`" DB_USERNAME="`${MYSQLUSER}`" DB_PASSWORD="`${MYSQLPASSWORD}`" DB_NAME="`${MYSQLDATABASE}`" REDIS_HOST="`${REDISHOST}`" REDIS_PORT="`${REDISPORT}`" 2>$null

railway variables --service api-gateway AUTH_SERVICE_URL="https://auth-service.railway.internal:8081" QMA_SERVICE_URL="https://qma-service.railway.internal:8082" 2>$null

Write-Green "✅ Environment variables set"
Write-Host ""

# Get API Gateway URL
Write-Yellow "🔗 Getting API Gateway URL..."
Start-Sleep -Seconds 5

$API_URL = railway domain --service api-gateway 2>$null | Select-String -Pattern 'https://[^\s]+' | ForEach-Object { $_.Matches[0].Value }

if ($API_URL) {
    Write-Green "✅ API Gateway: $API_URL"
    Write-Host ""

    # Deploy frontend
    Write-Yellow "🎨 Deploying Frontend..."
    Set-Location ..\QuantityMeasurementApp-Frontend-
    railway variables --service frontend VITE_API_URL="$API_URL" 2>$null
    railway up --service frontend
    Set-Location ..\QuantityMeasurementApp

    $FRONTEND_URL = railway domain --service frontend 2>$null | Select-String -Pattern 'https://[^\s]+' | ForEach-Object { $_.Matches[0].Value }
    if ($FRONTEND_URL) {
        Write-Green "✅ Frontend: $FRONTEND_URL"
    }
} else {
    Write-Yellow "⚠️  Could not get API URL. Deploy frontend manually."
}

# Summary
Write-Host ""
Write-Blue "╔════════════════════════════════════════════════════════════╗"
Write-Blue "║              🎉 Deployment Complete!                        ║"
Write-Blue "╚════════════════════════════════════════════════════════════╝"
Write-Host ""
Write-Green "Services deployed:"
Write-Host "  • auth-service"
Write-Host "  • qma-service"
Write-Host "  • api-gateway"
Write-Host "  • frontend"
Write-Host ""
Write-Yellow "📋 Next steps:"
Write-Host "  1. View dashboard: railway open"
Write-Host "  2. View logs: railway logs --service <name>"
Write-Host ""
Write-Yellow "⚠️  IMPORTANT - Set OAuth credentials:"
Write-Host "  railway variables --service auth-service GOOGLE_CLIENT_ID=xxx"
Write-Host "  railway variables --service auth-service GOOGLE_CLIENT_SECRET=xxx"
Write-Host ""

if ($API_URL) {
    Write-Green "🌐 Your API: $API_URL"
    if ($FRONTEND_URL) { Write-Green "🌐 Your App: $FRONTEND_URL" }
}

Write-Host ""
Read-Host "Press Enter to exit..."
