#!/bin/bash
# Deploy all services to Railway

set -e

echo "🚀 Railway Deployment Guide"
echo ""

# Check if railway CLI is installed
if ! command -v railway &> /dev/null; then
    echo "❌ Railway CLI not found. Install it first:"
    echo "   npm install -g @railway/cli"
    exit 1
fi

# Check if logged in
if ! railway whoami &> /dev/null; then
    echo "❌ Not logged in. Run: railway login"
    exit 1
fi

echo "✅ Railway CLI is ready"
echo ""

# Step 1: Create/link project
echo "Step 1: Creating Railway project..."
echo "   Command: railway init --name qma-app"
railway init --name qma-app || echo "   Project already exists or using existing"
echo ""

# Step 2: Add databases
echo "Step 2: Adding MySQL database..."
railway add --database mysql || echo "   MySQL already exists"
echo ""

echo "Step 3: Adding Redis..."
railway add --database redis || echo "   Redis already exists"
echo ""

# Step 3: Set shared environment variables
echo "Step 4: Setting environment variables..."
echo "   You'll need to set these manually in Railway dashboard:"
echo ""
echo "   JWT_SECRET=$(openssl rand -base64 32)"
echo "   GOOGLE_CLIENT_ID=your_google_client_id"
echo "   GOOGLE_CLIENT_SECRET=your_google_client_secret"
echo ""
read -p "Press Enter to open Railway dashboard..."
railway open &
echo ""

# Step 4: Deploy services
echo "Step 5: Deploying services..."
echo ""

# Auth Service
echo "🔐 Deploying Auth Service..."
cd auth-service
railway link --service auth-service 2>/dev/null || railway up --service auth-service
cd ..

# QMA Service
echo "📊 Deploying QMA Service..."
cd qma-service
railway link --service qma-service 2>/dev/null || railway up --service qma-service
cd ..

# API Gateway
echo "🌐 Deploying API Gateway..."
cd api-gateway-service
railway link --service api-gateway 2>/dev/null || railway up --service api-gateway
cd ..

# Get API Gateway URL
echo ""
echo "⏳ Waiting for API Gateway to be ready..."
sleep 5
API_URL=$(railway status --json 2>/dev/null | grep -o '"api-gateway":"[^"]*"' | cut -d'"' -f4 || echo "")

if [ -n "$API_URL" ]; then
    echo "✅ API Gateway URL: $API_URL"
    echo ""
    echo "🎨 Deploying Frontend..."
    cd ../QuantityMeasurementApp-Frontend-
    railway variables set VITE_API_URL="$API_URL"
    railway link --service frontend 2>/dev/null || railway up --service frontend
    cd ../QuantityMeasurementApp
else
    echo "⚠️ Could not get API Gateway URL automatically."
    echo "   After deployment, set VITE_API_URL manually in frontend service."
fi

echo ""
echo "✅ Deployment complete!"
echo ""
echo "📋 Next steps:"
echo "   1. Check Railway dashboard: railway open"
echo "   2. View logs: railway logs"
echo "   3. Set VITE_API_URL in frontend if not set automatically"
echo ""
