package com.cx.aiteaching.service.impl;

import com.cx.aiteaching.service.ChatService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ChatServiceImpl implements ChatService {

    private static final String API_URL = "https://spark-api-open.xf-yun.com/v1/chat/completions";
    private static final String API_SECRET = "YjBjNDAyMmE3NDc0N2U3Y2EyNzI5NmZj";
    private static final String API_KEY = "dce851f412509fca0f3188e488f3ba09";

    private final OkHttpClient client;
    private final Gson gson;

    public ChatServiceImpl() {
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();
        this.gson = new Gson();
    }

    @Override
    public void generateReply(String userMessage, WebSocketSession session) throws IOException {
        // 构建请求体
        String jsonRequest = buildJsonRequest(userMessage);

        // 创建请求
        RequestBody body = RequestBody.create(jsonRequest, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY + ":" + API_SECRET)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        // 发送请求并处理响应
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    session.sendMessage(new TextMessage("Error: " + e.getMessage()));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    session.sendMessage(new TextMessage("Unexpected code " + response));
                    return;
                }

                // 处理流式响应
                String responseBody = response.body().string();
                JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                JsonArray choicesArray = jsonResponse.getAsJsonArray("choices");
                for (int i = 0; i < choicesArray.size(); i++) {
                    JsonObject choiceObject = choicesArray.get(i).getAsJsonObject();
                    JsonObject messageObject = choiceObject.getAsJsonObject("message");
                    String content = messageObject.get("content").getAsString();
                    for (char c : content.toCharArray()) {
                        if (session.isOpen()) {
                            try {
                                session.sendMessage(new TextMessage(String.valueOf(c)));
                                Thread.sleep(50); // 模拟流式传输
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                break;
                            }
                        } else {
                            System.err.println("WebSocket session is closed.");
                            break;
                        }
                    }
                }
            }
        });
    }

    private String buildJsonRequest(String userMessage) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("model", "generalv3.5");
        JsonArray messagesArray = new JsonArray();
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("role", "user");
        messageObject.addProperty("content", userMessage);
        messagesArray.add(messageObject);
        jsonObject.add("messages", messagesArray);
        return jsonObject.toString();
    }
}
