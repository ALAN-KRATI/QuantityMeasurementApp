package com.apigateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

@RestController
public class GatewayController {

    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

    @Value("${AUTH_SERVICE_URL:http://localhost:8081}")
    private String authServiceUrl;

    @Value("${QMA_SERVICE_URL:http://localhost:8082}")
    private String qmaServiceUrl;

    private final RestTemplate restTemplate;

    public GatewayController() {
        this.restTemplate = new RestTemplate();
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> handleOptions() {
        logger.debug("Handling OPTIONS request");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        headers.add("Access-Control-Allow-Headers", "*");
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/auth/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseEntity<byte[]> proxyAuth(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        logger.debug("Proxying auth request: {}", request.getRequestURI());
        return proxy(request, body, authServiceUrl);
    }

    @RequestMapping(value = "/oauth2/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseEntity<byte[]> proxyOAuth2(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        logger.debug("Proxying OAuth2 request: {}", request.getRequestURI());
        return proxy(request, body, authServiceUrl);
    }

    @RequestMapping(value = "/login/oauth2/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseEntity<byte[]> proxyLoginOAuth2(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        logger.debug("Proxying login/oauth2 request: {}", request.getRequestURI());
        return proxy(request, body, authServiceUrl);
    }

    @RequestMapping(value = "/quantities/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.PATCH})
    public ResponseEntity<byte[]> proxyQuantities(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        logger.debug("Proxying quantities request: {}", request.getRequestURI());
        return proxy(request, body, qmaServiceUrl);
    }

    private ResponseEntity<byte[]> proxy(HttpServletRequest request, byte[] body, String baseUrl) {
        try {
            String targetPath = request.getRequestURI();
            String queryString = request.getQueryString();

            String targetUrl = baseUrl + targetPath;
            if (queryString != null && !queryString.isEmpty()) {
                targetUrl += "?" + queryString;
            }

            HttpMethod method = HttpMethod.valueOf(request.getMethod());
            URI uri = URI.create(targetUrl);

            HttpHeaders headers = new HttpHeaders();
            Collections.list(request.getHeaderNames()).forEach(headerName -> {
                headers.addAll(headerName, Collections.list(request.getHeaders(headerName)));
            });

            HttpEntity<byte[]> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(uri, method, requestEntity, byte[].class);

            HttpHeaders responseHeaders = new HttpHeaders();
            response.getHeaders().forEach((name, values) -> {
                if (!name.equalsIgnoreCase("Transfer-Encoding") && !name.equalsIgnoreCase("Content-Length")) {
                    responseHeaders.addAll(name, values);
                }
            });

            return new ResponseEntity<>(
                    response.getBody(),
                    responseHeaders,
                    response.getStatusCode()
            );

        } catch (HttpStatusCodeException e) {
            logger.warn("Backend returned error: {}", e.getStatusCode());
            return new ResponseEntity<>(
                    e.getResponseBodyAsByteArray(),
                    e.getResponseHeaders(),
                    e.getStatusCode()
            );
        } catch (Exception e) {
            logger.error("Gateway error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Gateway Error: " + e.getMessage()).getBytes());
        }
    }
}
