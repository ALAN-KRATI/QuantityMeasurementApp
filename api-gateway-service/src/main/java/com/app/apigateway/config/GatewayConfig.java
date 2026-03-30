package com.app.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.util.Enumeration;

import static org.springframework.web.servlet.function.RequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class GatewayConfig {

    @Value("${AUTH_SERVICE_URL:http://localhost:8081}")
    private String authServiceUrl;

    @Value("${QMA_SERVICE_URL:http://localhost:8082}")
    private String qmaServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route()
                // Auth service routes
                .route(path("/auth/**"), request -> {
                    String targetUrl = authServiceUrl + request.uri().getPath();
                    return proxyRequest(request, targetUrl);
                })
                .route(path("/oauth2/**"), request -> {
                    String targetUrl = authServiceUrl + request.uri().getPath();
                    if (request.uri().getQuery() != null) {
                        targetUrl += "?" + request.uri().getRawQuery();
                    }
                    return proxyRequest(request, targetUrl);
                })
                .route(path("/login/oauth2/**"), request -> {
                    String targetUrl = authServiceUrl + request.uri().getPath();
                    if (request.uri().getQuery() != null) {
                        targetUrl += "?" + request.uri().getRawQuery();
                    }
                    return proxyRequest(request, targetUrl);
                })
                // QMA service routes
                .route(path("/quantities/**"), request -> {
                    String targetUrl = qmaServiceUrl + request.uri().getPath();
                    return proxyRequest(request, targetUrl);
                })
                .build();
    }

    private ServerResponse proxyRequest(org.springframework.web.servlet.function.ServerRequest request, String targetUrl) {
        try {
            HttpMethod method = request.method();
            URI uri = URI.create(targetUrl);

            // Build headers
            org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
            request.headers().asHttpHeaders().forEach((name, values) -> {
                headers.put(name, values);
            });

            // Get body if present
            byte[] body = request.body(byte[].class);

            // Create request entity
            org.springframework.http.HttpEntity<byte[]> requestEntity = new org.springframework.http.HttpEntity<>(
                    body != null && body.length > 0 ? body : null,
                    headers
            );

            // Execute request
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    uri,
                    method,
                    requestEntity,
                    byte[].class
            );

            // Build response
            ServerResponse.BodyBuilder responseBuilder = ServerResponse
                    .status(response.getStatusCode());

            // Copy response headers
            response.getHeaders().forEach((name, values) -> {
                values.forEach(value -> responseBuilder.header(name, value));
            });

            return responseBuilder.body(response.getBody() != null ? response.getBody() : new byte[0]);

        } catch (Exception e) {
            return ServerResponse.status(500).body("Gateway error: " + e.getMessage());
        }
    }
}