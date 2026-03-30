#!/bin/bash
# Simple Railway Deployment - Works with current CLI

set -e

echo "🚀 Railway Deployment"
echo ""

# Login check
if ! railway whoami &>/dev/null; then
    echo "Please login: railway login"
    exit 1
fi

# Link project
echo "Linking to Railway project..."
railway link || railway init --name qma-app

# Add databases
echo "Adding databases (if not exist)..."
railway add --database mysql 2>/dev/null || true
railway add --database redis 2>/dev/null || true

echo ""
echo "🚀 Deploying services..."

# Deploy auth-service
echo ""
echo "1/4 Deploying auth-service..."
cd auth-service
railway up
cd ..

# Deploy qma-service
echo ""
echo "2/4 Deploying qma-service..."
cd qma-service
railway up
cd ..

# Deploy api-gateway
echo ""
echo "3/4 Deploying api-gateway..."
cd api-gateway-service
railway up
cd ..

# Get API URL and deploy frontend
echo ""
echo "4/4 Deploying frontend..."
cd ../QuantityMeasurementApp-Frontend-

# Try to get API URL
API_URL=$(cd ../QuantityMeasurementApp && railway domain 2>/dev/null | head -1 || echo "")
if [ -n "$API_URL" ]; then
    echo "Setting API URL: $API_URL"
    railway variables set VITE_API_URL="$API_URL" 2>/dev/null || true
fi

railway up
cd ../QuantityMeasurementApp

echo ""
echo "✅ All services deployed!"
echo ""
echo "Set OAuth credentials:"
echo "  railway variables --service auth-service GOOGLE_CLIENT_ID=xxx"
echo "  railway variables --service auth-service GOOGLE_CLIENT_SECRET=xxx"
echo ""
echo "View dashboard: railway open"
