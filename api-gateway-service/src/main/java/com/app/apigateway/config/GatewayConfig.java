package com.app.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Value("${AUTH_SERVICE_URL:http://localhost:8081}")
    private String authServiceUrl;

    @Value("${QMA_SERVICE_URL:http://localhost:8082}")
    private String qmaServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route()

                // OAuth2 routes - strip /api prefix, keep original path
                .route(
                        path("/api/oauth2/**"),
                        http(authServiceUrl)
                )
                .before(rewritePath(
                        "/api/(?<segment>.*)",
                        "/${segment}"
                ))

                // Login OAuth2 callback routes
                .route(
                        path("/api/login/oauth2/**"),
                        http(authServiceUrl)
                )
                .before(rewritePath(
                        "/api/(?<segment>.*)",
                        "/${segment}"
                ))

                // Auth routes (login/register) - strip /api prefix, add /auth
                .route(
                        path("/api/auth/**"),
                        http(authServiceUrl)
                )
                .before(rewritePath(
                        "/api/auth/(?<segment>.*)",
                        "/auth/${segment}"
                ))

                // Quantities routes - strip /api prefix, keep original
                .route(
                        path("/api/quantities/**"),
                        http(qmaServiceUrl)
                )
                .before(rewritePath(
                        "/api/(?<segment>.*)",
                        "/${segment}"
                ))

                // Legacy routes without /api prefix (backward compatibility)
                .route(
                        path("/auth/**"),
                        http(authServiceUrl)
                )
                .route(
                        path("/quantities/**"),
                        http(qmaServiceUrl)
                )
                .route(
                        path("/oauth2/**"),
                        http(authServiceUrl)
                )
                .route(
                        path("/login/oauth2/**"),
                        http(authServiceUrl)
                )

                .build();
    }
}