package com.app.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

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
                // Handle CORS preflight
                .route(path("/**").and(request -> request.method() == HttpMethod.OPTIONS), request ->
                    ServerResponse.ok()
                        .header("Access-Control-Allow-Origin", request.headers().firstHeader("Origin") != null ? request.headers().firstHeader("Origin") : "*")
                        .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
                        .header("Access-Control-Allow-Headers", "*")
                        .header("Access-Control-Allow-Credentials", "true")
                        .build()
                )
                // Auth service routes
                .route(path("/auth/**"), request -> proxy(request, authServiceUrl))
                .route(path("/oauth2/**"), request -> proxy(request, authServiceUrl))
                .route(path("/login/oauth2/**"), request -> proxy(request, authServiceUrl))
                // QMA service routes
                .route(path("/quantities/**"), request -> proxy(request, qmaServiceUrl))
                .build();
    }

    private ServerResponse proxy(org.springframework.web.servlet.function.ServerRequest request, String baseUrl) {
        try {
            HttpMethod method = request.method();
            String path = request.uri().getPath();
            String query = request.uri().getQuery();

            String targetUrl = baseUrl + path + (query != null ? "?" + query : "");
            URI uri = URI.create(targetUrl);

            // Build headers
            HttpHeaders headers = new HttpHeaders();
            request.headers().asHttpHeaders().forEach((name, values) -> headers.addAll(name, values));

            // Get body for POST/PUT/PATCH
            byte[] body = null;
            if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
                try {
                    body = request.body(byte[].class);
                } catch (Exception ignored) {}
            }

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(uri, method, requestEntity, byte[].class);

            ServerResponse.BodyBuilder builder = ServerResponse.status(response.getStatusCode());

            // Copy headers
            response.getHeaders().forEach((name, values) -> {
                if (!name.equalsIgnoreCase("Transfer-Encoding") && !name.equalsIgnoreCase("Content-Length")) {
                    builder.header(name, String.join(",", values));
                }
            });

            // Add CORS
            String origin = request.headers().firstHeader("Origin");
            builder.header("Access-Control-Allow-Origin", origin != null ? origin : "*");
            builder.header("Access-Control-Allow-Credentials", "true");

            return builder.body(response.getBody() != null ? response.getBody() : new byte[0]);

        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            return ServerResponse.status(e.getStatusCode())
                .header("Access-Control-Allow-Origin", request.headers().firstHeader("Origin") != null ? request.headers().firstHeader("Origin") : "*")
                .body(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            return ServerResponse.status(500)
                .header("Access-Control-Allow-Origin", request.headers().firstHeader("Origin") != null ? request.headers().firstHeader("Origin") : "*")
                .body(("Gateway error: " + e.getMessage()).getBytes());
        }
    }
}
