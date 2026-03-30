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
                // Handle OPTIONS preflight for all routes
                .route(request -> request.method() == HttpMethod.OPTIONS, request -> {
                    return ServerResponse.ok()
                            .header("Access-Control-Allow-Origin", "*")
                            .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                            .header("Access-Control-Allow-Headers", "*")
                            .build();
                })
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
            request.headers().asHttpHeaders().forEach(headers::putAll);

            // Get body if present (only for POST/PUT/PATCH)
            byte[] body = null;
            if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
                try {
                    body = request.body(byte[].class);
                } catch (Exception ignored) {
                    // No body present
                }
            }

            // Create request entity
            org.springframework.http.HttpEntity<byte[]> requestEntity = new org.springframework.http.HttpEntity<>(
                    body,
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

            // Copy response headers (skip problematic ones)
            response.getHeaders().forEach((name, values) -> {
                if (!name.equalsIgnoreCase("Transfer-Encoding") && !name.equalsIgnoreCase("Content-Length")) {
                    values.forEach(value -> responseBuilder.header(name, value));
                }
            });

            return responseBuilder.body(response.getBody() != null ? response.getBody() : new byte[0]);

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            return ServerResponse.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (org.springframework.web.client.HttpServerErrorException e) {
            return ServerResponse.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            return ServerResponse.status(500).body(("Gateway error: " + e.getMessage()).getBytes());
        }
    }
}