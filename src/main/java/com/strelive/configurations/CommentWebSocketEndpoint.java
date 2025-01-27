package com.strelive.configurations;

import com.strelive.services.StreamService;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/comments/{streamId}", configurator = WebSocketConfigurator.class)
public class CommentWebSocketEndpoint {

    //    private static final CopyOnWriteArrayList<CommentWebSocketEndpoint> allClients = new CopyOnWriteArrayList<>();
    private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap<>();
    private Session session;
    private String streamId;

    @Inject
    private StreamService streamService;

    @OnOpen
    public void onOpen(Session session, @PathParam("streamId") String streamId) {
        this.session = session;
        this.streamId = streamId;
//        allClients.add(this);
        rooms.computeIfAbsent(streamId, k -> new CopyOnWriteArraySet<>()).add(session);

        // JWT
//        String jwt = session.getRequestParameterMap().get("token").get(0); // Example: pass JWT in the URL or headers
//
//        // Validate JWT and extract user information (you can use a JWT library here)
//        String username = getUsernameFromJWT(jwt);  // Assuming you have a method to parse JWT
//
//        // Store user data in session properties or elsewhere as needed
//        session.getUserProperties().put("username", username);

        try {
            session.getBasicRemote().sendText("Connected to stream: " + streamId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        // additional logic to persist the comment or send it to other services

        // Broadcast the message to all clients of the specific stream
//        for (CommentWebSocketEndpoint client : allClients) {
//            if (client.streamId.equals(this.streamId)) {
//                try {
//                    client.session.getBasicRemote().sendText(message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        // Broadcast the message to all clients of the specific stream
        Set<Session> room = rooms.get(this.streamId);
        if (room != null) {
            for (Session client : room) {
                try {
                    client.getBasicRemote().sendText(message);  // Send the message to each client
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @OnClose
    public void onClose() {
//        allClients.remove(this);
        Set<Session> room = rooms.get(this.streamId);
        if (room != null) {
            room.remove(this.session);

            // If the room is empty, remove the room from the map
            if (room.isEmpty()) {
                rooms.remove(this.streamId);
            }
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }
}
