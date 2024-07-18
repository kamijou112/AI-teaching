package com.cx.aiteaching.controller;

import com.cx.aiteaching.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@RestController
public class ChatWebSocketController extends TextWebSocketHandler {

    @Autowired
    private ChatService chatService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        try {
            String payload = message.getPayload();
            System.out.println("Received message: " + payload);
            chatService.generateReply(payload, session);
        } catch (Exception e) {
            session.sendMessage(new TextMessage("Error processing message: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Connection established
        System.out.println("Connection established: " + session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        // Handle error
        System.err.println("Transport error: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Connection closed
        System.out.println("Connection closed: " + session.getId());
    }
}
