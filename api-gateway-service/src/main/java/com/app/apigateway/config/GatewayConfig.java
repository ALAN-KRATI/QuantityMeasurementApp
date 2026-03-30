package com.app.apigateway.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.HandlerMapping;
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
                .route(path("/auth/**"), this::handleAuth)
                .route(path("/oauth2/**"), this::handleAuth)
                .route(path("/login/oauth2/**"), this::handleAuth)
                .route(path("/quantities/**"), this::handleQma)
                .build();
    }

    private ServerResponse handleAuth(org.springframework.web.servlet.function.ServerRequest request) {
        return proxy(request, authServiceUrl);
    }

    private ServerResponse handleQma(org.springframework.web.servlet.function.ServerRequest request) {
        return proxy(request, qmaServiceUrl);
    }

    private ServerResponse proxy(org.springframework.web.servlet.function.ServerRequest request, String baseUrl) {
        try {
            HttpMethod method = request.method();
            String path = request.uri().getPath();
            String query = request.uri().getRawQuery();

            String targetUrl = baseUrl + path + (query != null ? "?" + query : "");
            URI uri = URI.create(targetUrl);

            // Build headers
            HttpHeaders headers = new HttpHeaders();
            request.headers().asHttpHeaders().forEach(headers::addAll);

            // Get body for POST/PUT/PATCH
            byte[] body = null;
            if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH) {
                try {
                    body = request.body(byte[].class);
                } catch (Exception e) {
                    // No body
                }
            }

            HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(uri, method, entity, byte[].class);

            ServerResponse.BodyBuilder builder = ServerResponse.status(response.getStatusCode());

            // Copy response headers (skip restricted ones)
            response.getHeaders().forEach((name, values) -> {
                if (!name.equalsIgnoreCase("Transfer-Encoding") && !name.equalsIgnoreCase("Content-Length")) {
                    builder.header(name, String.join(",", values));
                }
            });

            // Add CORS headers
            String origin = request.headers().firstHeader("Origin");
            builder.header("Access-Control-Allow-Origin", origin != null ? origin : "*");
            builder.header("Access-Control-Allow-Credentials", "true");

            return builder.body(response.getBody() != null ? response.getBody() : new byte[0]);

        } catch (Exception e) {
            String origin = request.headers().firstHeader("Origin");
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Access-Control-Allow-Origin", origin != null ? origin : "*")
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }
}
