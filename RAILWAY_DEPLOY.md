# Railway Deployment Guide

## Overview
Railway deploys services directly from GitHub. Each folder with a `Dockerfile` becomes a service.

## Step-by-Step Deployment

### 1. Push Code to GitHub
```bash
git add .
git commit -m "Add Railway deployment config"
git push origin main
```

### 2. Create Railway Project
1. Go to https://railway.app
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Choose your repository

### 3. Add Databases
In Railway dashboard:
1. Click "New"
2. Select "Database" → "Add MySQL"
3. Click "New"
4. Select "Database" → "Add Redis"

### 4. Deploy Services
For each service folder, create a new service:

#### Auth Service
1. Click "New" → "GitHub Repo"
2. Select repo, set **Root Directory**: `auth-service`
3. Railway auto-detects Dockerfile
4. Go to "Variables" tab, add:
   ```
   DB_HOST = ${MYSQLHOST}
   DB_PORT = ${MYSQLPORT}
   DB_USERNAME = ${MYSQLUSER}
   DB_PASSWORD = ${MYSQLPASSWORD}
   DB_NAME = ${MYSQLDATABASE}
   JWT_SECRET = (generate: openssl rand -base64 32)
   JWT_EXPIRATION = 3600000
   GOOGLE_CLIENT_ID = (from Google Cloud Console)
   GOOGLE_CLIENT_SECRET = (from Google Cloud Console)
   OAUTH2_REDIRECT_URI = (frontend URL)/oauth2-success
   ```

#### QMA Service
1. Click "New" → "GitHub Repo"
2. Set **Root Directory**: `qma-service`
3. Add Variables:
   ```
   DB_HOST = ${MYSQLHOST}
   DB_PORT = ${MYSQLPORT}
   DB_USERNAME = ${MYSQLUSER}
   DB_PASSWORD = ${MYSQLPASSWORD}
   DB_NAME = ${MYSQLDATABASE}
   JWT_SECRET = (same as auth-service)
   JWT_EXPIRATION = 3600000
   REDIS_HOST = ${REDISHOST}
   REDIS_PORT = ${REDISPORT}
   ```

#### API Gateway
1. Click "New" → "GitHub Repo"
2. Set **Root Directory**: `api-gateway-service`
3. Add Variables:
   ```
   AUTH_SERVICE_URL = http://auth-service.railway.internal:8081
   QMA_SERVICE_URL = http://qma-service.railway.internal:8082
   ```
   Note: Use the internal Railway DNS names

#### Frontend
1. Click "New" → "GitHub Repo"
2. Set **Root Directory**: `QuantityMeasurementApp-Frontend-`
3. Add Variables:
   ```
   VITE_API_URL = (your API Gateway public URL)
   ```

### 5. Networking / Domain Setup
Each service gets a public URL by default. To customize:

1. Go to service settings
2. Click "Settings" → "Domains"
3. Click "Generate Domain" or add custom domain

### 6. Update OAuth Redirect URI
In Google Cloud Console:
1. Go to Credentials → OAuth 2.0 Client IDs
2. Edit your client
3. Add authorized redirect URI:
   ```
   https://(your-auth-service)/login/oauth2/code/google
   ```
4. Add JavaScript origin:
   ```
   https://(your-frontend-service)
   ```

## Alternative: Using Railway CLI

```bash
# Install CLI
npm install -g @railway/cli

# Login
railway login

# Link to project
railway link

# Deploy specific service
cd auth-service
railway up

# Or deploy all
cd .. && railway up
```

## Troubleshooting

### Services can't connect to each other
- Use Railway's internal DNS: `servicename.railway.internal`
- Check if services are in the same environment

### Database connection failed
- Verify env vars are set correctly
- Check MySQL service is running
- Use `${MYSQLHOST}` not `localhost`

### Frontend can't reach backend
- Check `VITE_API_URL` is set to API Gateway public URL
- Ensure CORS is configured on gateway

## Costs
- MySQL: 500 MB free
- Redis: 50 MB free
- Services: $5/month credit (enough for 1-2 services always-on)
- Idle services sleep after inactivity (free tier)
