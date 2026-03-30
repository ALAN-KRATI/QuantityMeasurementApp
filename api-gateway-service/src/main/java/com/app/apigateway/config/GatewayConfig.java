package com.app.apigateway.config;

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

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route()

                // OAuth routes
                .route(
                        path("/api/auth/oauth2/**"),
                        http("http://auth-service:8081")
                )
                .before(rewritePath(
                        "/api/auth/(?<segment>.*)",
                        "/${segment}"
                ))

                // Login/Register routes
                .route(
                        path("/api/auth/**"),
                        http("http://auth-service:8081")
                )
                .before(rewritePath(
                        "/api/auth/(?<segment>.*)",
                        "/auth/${segment}"
                ))

                // QMA routes
                .route(
                        path("/api/qma/**"),
                        http("http://qma-service:8082")
                )
                .before(rewritePath(
                        "/api/qma/(?<segment>.*)",
                        "/quantities/${segment}"
                ))

                .build();
    }
}