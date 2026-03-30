#!/bin/bash
# Fully Automated Railway Deployment Script

set -e

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

# Default values
JWT_SECRET="${JWT_SECRET:-dGVzdGRHVnpkR1JsYzNSbFpHVnlkR2x1WjNOaFkzSmxkR2x2YmkxclpYaz0=}"
DB_USER="${DB_USER:-root}"
DB_PASS="${DB_PASS:-$(openssl rand -base64 12)}"

echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║     🚀 Fully Automated Railway Deployment Script          ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Check prerequisites
check_prereqs() {
    echo -e "${YELLOW}🔍 Checking prerequisites...${NC}"

    if ! command -v railway &> /dev/null; then
        echo -e "${RED}❌ Railway CLI not found. Installing...${NC}"
        npm install -g @railway/cli
    fi

    if ! command -v git &> /dev/null; then
        echo -e "${RED}❌ Git not found${NC}"
        exit 1
    fi

    if ! command -v gh &> /dev/null; then
        echo -e "${YELLOW}⚠️  GitHub CLI not found. Install for auto-repo creation:${NC}"
        echo "   brew install gh"
        echo ""
    fi

    echo -e "${GREEN}✅ Prerequisites checked${NC}"
}

# GitHub repo setup
setup_github() {
    echo ""
    echo -e "${YELLOW}📦 Setting up GitHub repository...${NC}"

    if [ ! -d .git ]; then
        git init
        git add .
        git commit -m "Initial commit"
    fi

    # Check if remote exists
    if ! git remote get-url origin &> /dev/null; then
        if command -v gh &> /dev/null; then
            echo "Creating GitHub repository..."
            gh repo create qma-app --public --source=. --remote=origin --push
        else
            echo -e "${RED}No GitHub remote found. Create repo manually:${NC}"
            echo "1. Go to https://github.com/new"
            echo "2. Create repo named 'qma-app'"
            echo "3. Run: git remote add origin https://github.com/YOUR_USERNAME/qma-app.git"
            echo "4. Run: git push -u origin main"
            read -p "Press Enter after completing..."
        fi
    else
        git add . 2>/dev/null || true
        git commit -m "Deploy to Railway" 2>/dev/null || true
        git push 2>/dev/null || true
    fi

    echo -e "${GREEN}✅ GitHub ready${NC}"
}

# Railway login
railway_login() {
    echo ""
    echo -e "${YELLOW}🔐 Checking Railway login...${NC}"

    if ! railway whoami &> /dev/null; then
        echo "Please login to Railway (browser will open):"
        railway login
    fi

    echo -e "${GREEN}✅ Logged in as: $(railway whoami)${NC}"
}

# Create Railway project
create_project() {
    echo ""
    echo -e "${YELLOW}📁 Creating Railway project...${NC}"

    if [ ! -f .railway/config.json ]; then
        # Create new project with empty template
        railway init --name qma-app --empty
    fi

    PROJECT_ID=$(railway status --json 2>/dev/null | grep -o '"id":"[^"]*"' | head -1 | cut -d'"' -f4 || echo "")
    echo -e "${GREEN}✅ Project ID: $PROJECT_ID${NC}"
}

# Add databases using Railway CLI
add_databases() {
    echo ""
    echo -e "${YELLOW}🗄️  Adding databases...${NC}"

    # Add MySQL
    echo "Creating MySQL database..."
    railway add --database mysql --name qma-mysql 2>/dev/null || echo "MySQL may already exist"

    # Add Redis
    echo "Creating Redis..."
    railway add --database redis --name qma-redis 2>/dev/null || echo "Redis may already exist"

    echo -e "${GREEN}✅ Databases added${NC}"
}

# Wait for database variables to be available
wait_for_db_vars() {
    echo ""
    echo -e "${YELLOW}⏳ Waiting for database to be ready...${NC}"
    sleep 10

    # Get database connection info
    MYSQL_HOST=$(railway variables --service qma-mysql 2>/dev/null | grep MYSQLHOST | cut -d'=' -f2 || echo "")

    if [ -z "$MYSQL_HOST" ]; then
        echo -e "${YELLOW}⚠️  Could not get DB vars automatically. Will use Railway references.${NC}"
    fi
}

# Deploy a service
deploy_service() {
    local name=$1
    local dir=$2

    echo ""
    echo -e "${YELLOW}🚀 Deploying $name...${NC}"

    cd "$dir"

    # Create and link service
    railway up --service "$name" || {
        echo -e "${RED}Failed to deploy $name${NC}"
        cd - > /dev/null
        return 1
    }

    cd - > /dev/null
    echo -e "${GREEN}✅ $name deployed${NC}"
}

# Set environment variables
set_env_vars() {
    echo ""
    echo -e "${YELLOW}⚙️  Setting environment variables...${NC}"

    # Auth Service
    echo "Configuring auth-service..."
    railway variables --service auth-service \
        JWT_SECRET="$JWT_SECRET" \
        JWT_EXPIRATION="3600000" \
        DB_HOST="\${{MYSQLHOST}}" \
        DB_PORT="\${{MYSQLPORT}}" \
        DB_USERNAME="\${{MYSQLUSER}}" \
        DB_PASSWORD="\${{MYSQLPASSWORD}}" \
        DB_NAME="\${{MYSQLDATABASE}}" 2>/dev/null || echo "Auth vars may already be set"

    # QMA Service
    echo "Configuring qma-service..."
    railway variables --service qma-service \
        JWT_SECRET="$JWT_SECRET" \
        JWT_EXPIRATION="3600000" \
        DB_HOST="\${{MYSQLHOST}}" \
        DB_PORT="\${{MYSQLPORT}}" \
        DB_USERNAME="\${{MYSQLUSER}}" \
        DB_PASSWORD="\${{MYSQLPASSWORD}}" \
        DB_NAME="\${{MYSQLDATABASE}}" \
        REDIS_HOST="\${{REDISHOST}}" \
        REDIS_PORT="\${{REDISPORT}}" 2>/dev/null || echo "QMA vars may already be set"

    # API Gateway
    echo "Configuring api-gateway..."
    railway variables --service api-gateway \
        AUTH_SERVICE_URL="https://auth-service.railway.internal:8081" \
        QMA_SERVICE_URL="https://qma-service.railway.internal:8082" 2>/dev/null || echo "Gateway vars may already be set"

    echo -e "${GREEN}✅ Environment variables set${NC}"
}

# Get service URL
get_service_url() {
    local service=$1
    railway domain --service "$service" 2>/dev/null | grep -o 'https://[^[:space:]]*' || echo ""
}

# Main deployment
main() {
    check_prereqs
    setup_github
    railway_login
    create_project
    add_databases

    # Deploy backend services
    deploy_service "auth-service" "auth-service"
    deploy_service "qma-service" "qma-service"
    deploy_service "api-gateway" "api-gateway-service"

    # Set environment variables
    set_env_vars

    # Get API Gateway URL
    echo ""
    echo -e "${YELLOW}🔗 Getting API Gateway URL...${NC}"
    sleep 5

    API_URL=$(get_service_url "api-gateway")

    if [ -n "$API_URL" ]; then
        echo -e "${GREEN}✅ API Gateway: $API_URL${NC}"

        # Deploy frontend
        echo ""
        echo -e "${YELLOW}🎨 Deploying Frontend...${NC}"
        cd ../QuantityMeasurementApp-Frontend-

        railway variables --service frontend VITE_API_URL="$API_URL" 2>/dev/null || true
        railway up --service frontend

        cd - > /dev/null

        FRONTEND_URL=$(get_service_url "frontend")
        echo -e "${GREEN}✅ Frontend: ${FRONTEND_URL:-Deployed}${NC}"
    else
        echo -e "${YELLOW}⚠️  Could not get API URL. Deploy frontend manually.${NC}"
    fi

    # Summary
    echo ""
    echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
    echo -e "${BLUE}║              🎉 Deployment Complete!                        ║${NC}"
    echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
    echo ""
    echo -e "${GREEN}Services deployed:${NC}"
    echo "  • auth-service"
    echo "  • qma-service"
    echo "  • api-gateway"
    echo "  • frontend"
    echo ""
    echo -e "${YELLOW}📋 Next steps:${NC}"
    echo "  1. View dashboard: railway open"
    echo "  2. View logs: railway logs --service <name>"
    echo ""
    echo -e "${YELLOW}⚠️  IMPORTANT - Set OAuth credentials:${NC}"
    echo "  railway variables --service auth-service GOOGLE_CLIENT_ID=xxx"
    echo "  railway variables --service auth-service GOOGLE_CLIENT_SECRET=xxx"
    echo ""

    if [ -n "$API_URL" ]; then
        echo -e "${GREEN}🌐 Your API: $API_URL${NC}"
        [ -n "$FRONTEND_URL" ] && echo -e "${GREEN}🌐 Your App: $FRONTEND_URL${NC}"
    fi

    echo ""
}

# Run main
main "$@"
