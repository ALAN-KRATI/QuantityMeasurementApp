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

    @Value("${AUTH_SERVICE_URL}")
    private String authServiceUrl;

    @Value("${QMA_SERVICE_URL}")
    private String qmaServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {

        return route()

                .route(path("/auth/**"), http(authServiceUrl))
                .before(rewritePath("/auth/(?<segment>.*)", "/auth/${segment}"))

                .route(path("/oauth2/**"), http(authServiceUrl))
                .before(rewritePath("/oauth2/(?<segment>.*)", "/oauth2/${segment}"))

                .route(path("/login/oauth2/**"), http(authServiceUrl))
                .before(rewritePath("/login/oauth2/(?<segment>.*)", "/login/oauth2/${segment}"))

                .route(path("/quantities/**"), http(qmaServiceUrl))
                .before(rewritePath("/quantities/(?<segment>.*)", "/quantities/${segment}"))

                .build();
    }
}