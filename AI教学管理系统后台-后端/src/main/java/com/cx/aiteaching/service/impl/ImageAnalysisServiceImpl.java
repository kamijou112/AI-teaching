package com.cx.aiteaching.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cx.aiteaching.service.ImageAnalysisService;
import com.google.gson.Gson;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class ImageAnalysisServiceImpl implements ImageAnalysisService {

    private static final String HOST_URL = "https://spark-api.cn-huabei-1.xf-yun.com/v2.1/image";
    private static final String APP_ID = "67a0339c";
    private static final String API_SECRET = "YmQxNmQyOTA1MDU4YjIxODA1NmUzZDYx";
    private static final String API_KEY = "b7c44ecfd5a60cc1de69ac7c996e8c25";
    private static final Gson GSON = new Gson();

    @Override
    public String analyzeImage(MultipartFile file, String question) throws IOException {
        String authUrl = getAuthUrl(HOST_URL, API_KEY, API_SECRET);
        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
        Request request = new Request.Builder().url(url).build();

        final CopyOnWriteArrayList<String> messages = new CopyOnWriteArrayList<>();

        CountDownLatch latch = new CountDownLatch(1);

        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                try {
                    String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
                    JSONObject requestJson = buildRequestJson(base64Image, question);
                    webSocket.send(requestJson.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("Received message: " + text); // 打印完整的响应消息以便调试
                JSONObject jsonResponse = JSON.parseObject(text);
                JSONArray textArray = jsonResponse.getJSONObject("payload").getJSONObject("choices").getJSONArray("text");
                if (textArray != null && !textArray.isEmpty()) {
                    messages.add(textArray.getJSONObject(0).getString("content"));
                }

            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                t.printStackTrace();
                latch.countDown();  // 当发生错误时，也减少计数以防止永远等待
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                webSocket.close(1000, null);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                System.out.println("WebSocket closed: " + reason);
                latch.countDown();  // 在连接关闭时减少计数
            }
        });

        // 等待响应处理，此处可以根据实际需要进行调整
        try {
            if (!latch.await(10, TimeUnit.SECONDS)) {
                System.out.println("Timeout waiting for response");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // 拼接所有接收到的消息
        StringBuilder responseText = new StringBuilder();
        for (String message : messages) {
            responseText.append(message);
        }
        return responseText.toString();
    }

    private JSONObject buildRequestJson(String base64Image, String question) {
        JSONObject requestJson = new JSONObject();

        JSONObject header = new JSONObject();
        header.put("app_id", APP_ID);
        header.put("uid", UUID.randomUUID().toString());

        JSONObject parameter = new JSONObject();
        JSONObject chat = new JSONObject();
        chat.put("domain", "image");
        chat.put("temperature", 0.5);
        chat.put("top_k", 4);
        chat.put("max_tokens", 2028);
        chat.put("auditing", "default");
        parameter.put("chat", chat);

        JSONObject payload = new JSONObject();
        JSONObject message = new JSONObject();
        JSONArray text = new JSONArray();

        JSONObject imageContent = new JSONObject();
        imageContent.put("role", "user");
        imageContent.put("content", base64Image);
        imageContent.put("content_type", "image");

        JSONObject questionContent = new JSONObject();
        questionContent.put("role", "user");
        questionContent.put("content", question);
        questionContent.put("content_type", "text");

        text.add(imageContent);
        text.add(questionContent);

        message.put("text", text);
        payload.put("message", message);

        requestJson.put("header", header);
        requestJson.put("parameter", parameter);
        requestJson.put("payload", payload);

        return requestJson;
    }

    private String getAuthUrl(String hostUrl, String apiKey, String apiSecret) {
        try {
            URL url = new URL(hostUrl);
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = format.format(new Date());

            String preStr = "host: " + url.getHost() + "\n" +
                    "date: " + date + "\n" +
                    "GET " + url.getPath() + " HTTP/1.1";

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(spec);

            byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
            String sha = Base64.getEncoder().encodeToString(hexDigits);

            String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                    apiKey, "hmac-sha256", "host date request-line", sha);

            return String.format("%s?authorization=%s&date=%s&host=%s",
                    hostUrl, Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8)), date, url.getHost());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
