package com.cx.aiteaching.service;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public interface ChatService {
    void generateReply(String userMessage, WebSocketSession session) throws IOException;
}