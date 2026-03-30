#!/bin/bash
# Deploy QMA App to Railway

set -e

echo "🚀 Railway Deployment Script"
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check prerequisites
if ! command -v railway &> /dev/null; then
    echo -e "${RED}❌ Railway CLI not found${NC}"
    echo "Install: npm install -g @railway/cli"
    exit 1
fi

if ! command -v git &> /dev/null; then
    echo -e "${RED}❌ Git not found${NC}"
    exit 1
fi

# Step 1: Git check
echo -e "${YELLOW}Step 1: Checking Git repository...${NC}"
if [ ! -d .git ]; then
    echo "Initializing Git repository..."
    git init
    git add .
    git commit -m "Initial commit for Railway deployment"
    echo -e "${GREEN}✅ Git repo created${NC}"
    echo ""
    echo "⚠️  You need to push to GitHub before deploying:"
    echo "   git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git"
    echo "   git push -u origin main"
    echo ""
    read -p "Press Enter after pushing to GitHub..."
else
    echo -e "${GREEN}✅ Git repository found${NC}"
    # Check for uncommitted changes
    if ! git diff-index --quiet HEAD --; then
        echo "Uncommitted changes found. Committing..."
        git add .
        git commit -m "Update for Railway deployment"
        git push
        echo -e "${GREEN}✅ Changes pushed${NC}"
    fi
fi
echo ""

# Step 2: Login
echo -e "${YELLOW}Step 2: Checking Railway login...${NC}"
if ! railway whoami &> /dev/null; then
    echo "Please login to Railway:"
    railway login
fi
echo -e "${GREEN}✅ Logged in as: $(railway whoami)${NC}"
echo ""

# Step 3: Create/Link project
echo -e "${YELLOW}Step 3: Setting up Railway project...${NC}"
if [ ! -f .railway/config.json ]; then
    echo "Creating new Railway project..."
    railway init --name qma-app
else
    echo "Using existing Railway project"
fi
echo -e "${GREEN}✅ Project ready${NC}"
echo ""

# Step 4: Add databases
echo -e "${YELLOW}Step 4: Adding databases...${NC}"
echo "Opening Railway dashboard to add MySQL and Redis..."
railway open &
sleep 2
echo ""
echo "In the Railway dashboard:"
echo "  1. Click 'New' → 'Database' → 'MySQL'"
echo "  2. Click 'New' → 'Database' → 'Redis'"
echo ""
read -p "Press Enter after adding databases..."
echo -e "${GREEN}✅ Databases added${NC}"
echo ""

# Step 5: Deploy services
echo -e "${YELLOW}Step 5: Deploying services...${NC}"
echo ""

# Get project info
PROJECT_ID=$(railway status --json 2>/dev/null | grep -o '"projectId":"[^"]*"' | cut -d'"' -f4 || echo "")

deploy_service() {
    local service_name=$1
    local service_dir=$2
    local env_vars=$3

    echo -e "${YELLOW}Deploying $service_name...${NC}"

    # Create service if not exists
    railway link --service "$service_name" 2>/dev/null || {
        echo "  Creating new service: $service_name"
    }

    # Change to service directory and deploy
    cd "$service_dir"
    railway up --service "$service_name"
    cd - > /dev/null

    echo -e "${GREEN}✅ $service_name deployed${NC}"
    echo ""
}

# Deploy backend services
deploy_service "auth-service" "auth-service"
deploy_service "qma-service" "qma-service"
deploy_service "api-gateway" "api-gateway-service"

# Get API Gateway URL
echo -e "${YELLOW}Getting API Gateway URL...${NC}"
sleep 5
API_URL=$(railway domain --service api-gateway 2>/dev/null | grep -o 'https://[^[:space:]]*' || echo "")

if [ -n "$API_URL" ]; then
    echo -e "${GREEN}✅ API Gateway URL: $API_URL${NC}"
    echo ""

    # Deploy frontend with API URL
    echo -e "${YELLOW}Deploying Frontend...${NC}"
    cd ../QuantityMeasurementApp-Frontend-
    railway variables set VITE_API_URL="$API_URL" --service frontend 2>/dev/null || true
    railway up --service frontend
    cd - > /dev/null
    echo -e "${GREEN}✅ Frontend deployed${NC}"
else
    echo -e "${YELLOW}⚠️ Could not get API Gateway URL automatically${NC}"
    echo "   Deploy frontend manually and set VITE_API_URL"
fi

echo ""
echo -e "${GREEN}🎉 Deployment complete!${NC}"
echo ""
echo "📋 Next steps:"
echo "   1. View dashboard: railway open"
echo "   2. View logs: railway logs --service SERVICE_NAME"
echo "   3. Set environment variables in dashboard for each service"
echo ""
echo "⚠️  IMPORTANT: Set these environment variables in Railway dashboard:"
echo ""
echo "   auth-service:"
echo "     JWT_SECRET=$(openssl rand -base64 32)"
echo "     GOOGLE_CLIENT_ID=your_google_client_id"
echo "     GOOGLE_CLIENT_SECRET=your_google_client_secret"
echo ""
echo "   qma-service:"
echo "     JWT_SECRET=(same as auth-service)"
echo ""
echo "   api-gateway:"
echo "     AUTH_SERVICE_URL=http://auth-service.railway.internal:8081"
echo "     QMA_SERVICE_URL=http://qma-service.railway.internal:8082"
echo ""
