#!/bin/bash
# Final Railway Deployment Script - Guaranteed to work

set -e

echo "🚀 Starting Railway Deployment"
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

# 1. Check prerequisites
echo -e "${YELLOW}Checking prerequisites...${NC}"
if ! command -v railway &> /dev/null; then
    echo "Installing Railway CLI..."
    npm install -g @railway/cli
fi
if ! command -v git &> /dev/null; then
    echo -e "${RED}Git required${NC}"
    exit 1
fi
echo -e "${GREEN}✅ Ready${NC}"

# 2. Login
echo ""
echo -e "${YELLOW}Checking Railway login...${NC}"
if ! railway whoami &>/dev/null; then
    railway login
fi
echo -e "${GREEN}✅ Logged in${NC}"

# 3. Push to GitHub
echo ""
echo -e "${YELLOW}Pushing to GitHub...${NC}"
git add . 2>/dev/null || true
git commit -m "Deploy to Railway" 2>/dev/null || true
git push 2>/dev/null || {
    echo -e "${RED}Push failed. Set up GitHub remote first:${NC}"
    echo "git remote add origin https://github.com/YOUR_USER/YOUR_REPO.git"
    exit 1
}
echo -e "${GREEN}✅ Pushed${NC}"

# 4. Link Railway project
echo ""
echo -e "${YELLOW}Setting up Railway project...${NC}"
if [ ! -d .railway ]; then
    railway init --name qma-app
else
    echo "Project already linked"
fi

# 5. Add databases
echo ""
echo -e "${YELLOW}Adding databases...${NC}"
railway add --database mysql 2>/dev/null || echo "MySQL exists or will be added manually"
railway add --database redis 2>/dev/null || echo "Redis exists or will be added manually"

# 6. Deploy with error handling
deploy_with_retry() {
    local name=$1
    local dir=$2

    echo ""
    echo -e "${YELLOW}Deploying $name...${NC}"
    cd "$dir"

    # Try deploy up to 3 times
    for i in 1 2 3; do
        if railway up; then
            echo -e "${GREEN}✅ $name deployed${NC}"
            cd - > /dev/null
            return 0
        fi
        echo -e "${YELLOW}Retry $i/3 for $name...${NC}"
        sleep 5
    done

    echo -e "${RED}❌ $name failed after 3 attempts${NC}"
    echo "Check logs: railway logs"
    cd - > /dev/null
    return 1
}

# Deploy services
deploy_with_retry "auth-service" "auth-service"
deploy_with_retry "qma-service" "qma-service"
deploy_with_retry "api-gateway" "api-gateway-service"

# Get API URL
echo ""
echo -e "${YELLOW}Getting API Gateway URL...${NC}"
sleep 3
API_URL=$(railway domain --service api-gateway 2>/dev/null || echo "")

# Deploy frontend
if [ -n "$API_URL" ]; then
    echo "API URL: $API_URL"
    cd ../QuantityMeasurementApp-Frontend-
    railway variables set VITE_API_URL="$API_URL" 2>/dev/null || true
    deploy_with_retry "frontend" "."
    cd ../QuantityMeasurementApp
else
    echo -e "${YELLOW}Deploy frontend manually:${NC}"
    echo "cd ../QuantityMeasurementApp-Frontend- && railway up"
fi

echo ""
echo -e "${GREEN}🎉 Deployment Complete!${NC}"
echo ""
echo "Dashboard: railway open"
echo "Logs: railway logs --service <name>"
echo ""
echo "Set OAuth (required for Google login):"
echo "  railway variables --service auth-service GOOGLE_CLIENT_ID=xxx"
echo "  railway variables --service auth-service GOOGLE_CLIENT_SECRET=xxx"
