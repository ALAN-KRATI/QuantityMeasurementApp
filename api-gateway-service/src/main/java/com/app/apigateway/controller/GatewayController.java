package com.app.apigateway.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Enumeration;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class GatewayController {

    @Value("${AUTH_SERVICE_URL:http://localhost:8081}")
    private String authServiceUrl;

    @Value("${QMA_SERVICE_URL:http://localhost:8082}")
    private String qmaServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @RequestMapping(value = "/auth/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<byte[]> authProxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        return proxy(request, body, authServiceUrl);
    }

    @RequestMapping(value = "/oauth2/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<byte[]> oauth2Proxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        return proxy(request, body, authServiceUrl);
    }

    @RequestMapping(value = "/login/oauth2/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<byte[]> loginOauth2Proxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        return proxy(request, body, authServiceUrl);
    }

    @RequestMapping(value = "/quantities/**", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
    public ResponseEntity<byte[]> quantitiesProxy(HttpServletRequest request, @RequestBody(required = false) byte[] body) {
        return proxy(request, body, qmaServiceUrl);
    }

    private ResponseEntity<byte[]> proxy(HttpServletRequest request, byte[] body, String baseUrl) {
        try {
            String path = request.getRequestURI();
            String query = request.getQueryString();
            String targetUrl = baseUrl + path + (query != null ? "?" + query : "");

            HttpMethod method = HttpMethod.valueOf(request.getMethod());
            URI uri = URI.create(targetUrl);

            HttpHeaders headers = new HttpHeaders();
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                headers.addAll(name, java.util.Collections.list(request.getHeaders(name)));
            }

            HttpEntity<byte[]> entity = new HttpEntity<>(body, headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(uri, method, entity, byte[].class);

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.putAll(response.getHeaders());
            responseHeaders.set("Access-Control-Allow-Origin", "*");
            responseHeaders.set("Access-Control-Allow-Credentials", "true");

            return new ResponseEntity<>(response.getBody(), responseHeaders, response.getStatusCode());

        } catch (Exception e) {
            HttpHeaders errorHeaders = new HttpHeaders();
            errorHeaders.set("Access-Control-Allow-Origin", "*");
            return new ResponseEntity<>(("Error: " + e.getMessage()).getBytes(), errorHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
