package com.app.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import org.springframework.web.servlet.function.HandlerFilterFunction;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;
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

                // OAuth2 authorization endpoint (initial login request)
                .route(
                        path("/oauth2/authorization/**"),
                        http(authServiceUrl)
                )

                // OAuth2 callback endpoint - proxy directly without rewrite
                .route(
                        path("/login/oauth2/code/*"),
                        http(authServiceUrl)
                )

                // Auth routes (login/register)
                .route(
                        path("/auth/**"),
                        http(authServiceUrl)
                )

                // Quantities routes
                .route(
                        path("/quantities/**"),
                        http(qmaServiceUrl)
                )

                .build();
    }
}