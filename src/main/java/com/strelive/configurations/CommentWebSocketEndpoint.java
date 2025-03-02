package com.strelive.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strelive.dto.CommentMessage;
import com.strelive.entities.User;
import com.strelive.services.StreamService;
import com.strelive.utils.AuthUtils;
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

    private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap<>();
    private Session session;
    private String streamId;
    private User user;
    @Inject
    private StreamService streamService;

    @OnOpen
    public void onOpen(Session session, @PathParam("streamId") String streamId) {
        this.session = session;
        this.streamId = streamId;
//        allClients.add(this);
        rooms.computeIfAbsent(streamId, k -> new CopyOnWriteArraySet<>()).add(session);

        // JWT
        String jwt = session.getRequestParameterMap().get("token").get(0);
        user = AuthUtils.getUserDetailFromToken(jwt);
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
                    CommentMessage commentMessage = new CommentMessage(user.getId(), user.getUsername(), user.getProfilePicture(), message);
                    String jsonMessage = new ObjectMapper().writeValueAsString(commentMessage);
                    client.getBasicRemote().sendText(jsonMessage);
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
