package com.strelive.configurations;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;
import jakarta.websocket.server.ServerEndpointConfig.Configurator;
import jakarta.websocket.Session;

import java.util.List;
import java.util.Map;

public class WebSocketConfigurator extends Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(config, request, response);

        // Extract Authorization header from the request
//        Map<String, List<String>> headers = request.getHeaders();
//        List<String> authorizationHeaders = headers.get("Sec-WebSocket-Protocol");

//        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
//            String token = authorizationHeaders.get(0).replace("Bearer ", "");
//            // Store the token or validate it
//            config.getUserProperties().put("token", token);  // Store JWT token in session properties
//        }

        config.getUserProperties().put("token", request.getParameterMap().get("token").get(0));
    }
}
