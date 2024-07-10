package com.cx.aiteaching.service.impl;

import cn.hutool.json.ObjectMapper;
import com.cx.aiteaching.service.AudioModel;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class AudioModelImpl implements AudioModel {

    @Value("${xfyun.api.key}")
    private String apiKey;

    @Value("${xfyun.api.secret}")
    private String apiSecret;

    @Value("${xfyun.app.id}")
    private String appId;

    private static final String API_URL = "wss://iat-api.xfyun.cn/v2/iat";

    @Override
    public String AM(File audioFile) {
        BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
        StringBuilder fullResult = new StringBuilder();
        try {
            String date = getServerTime();
            String signature = generateSignature(date);
            String encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8.toString());
            String uriString = API_URL + "?authorization=" + signature + "&date=" + encodedDate + "&host=iat-api.xfyun.cn";
            URI uri = new URI(uriString);

            WebSocketClient client = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("Connection opened");
                    new Thread(() -> sendAudioFile(audioFile, this)).start();
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Received message: " + message);
                    try {
                        JSONObject rootNode = new JSONObject(message);
                        JSONObject resultNode = rootNode.getJSONObject("data").getJSONObject("result");
                        JSONArray wsArray = resultNode.getJSONArray("ws");

                        for (int i = 0; i < wsArray.length(); i++) {
                            JSONObject wsNode = wsArray.getJSONObject(i);
                            JSONArray cwArray = wsNode.getJSONArray("cw");
                            for (int j = 0; j < cwArray.length(); j++) {
                                JSONObject cwNode = cwArray.getJSONObject(j);
                                String word = cwNode.getString("w");
                                fullResult.append(word);
                            }
                        }

                        boolean isLastFrame = resultNode.getBoolean("ls");
                        if (isLastFrame) {
                            messageQueue.offer(fullResult.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Connection closed with exit code " + code + " additional info: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.err.println("An error occurred: " + ex.getMessage());
                }
            };

            if (client.connectBlocking(10, TimeUnit.SECONDS)) {
                // Wait for the message from WebSocket and return it
                return messageQueue.poll(30, TimeUnit.SECONDS);
            } else {
                return "Error: Unable to establish WebSocket connection";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error during recognition: " + e.getMessage();
        }
    }

    private void sendAudioFile(File audioFile, WebSocketClient client) {
        try (FileInputStream fis = new FileInputStream(audioFile)) {
            // 发送JSON头
            String headerJson = "{\"common\":{\"app_id\":\"" + appId + "\"},\"business\":{\"domain\":\"iat\",\"language\":\"zh_cn\",\"accent\":\"mandarin\",\"vad_eos\":10000},\"data\":{\"status\":0,\"format\":\"audio/L16;rate=16000\",\"encoding\":\"raw\",\"audio\":\"\"}}";
            client.send(headerJson);

            byte[] buffer = new byte[1280]; // 一帧音频大小
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                byte[] tempBuffer = new byte[bytesRead];
                System.arraycopy(buffer, 0, tempBuffer, 0, bytesRead);
                String audioBase64 = Base64.getEncoder().encodeToString(tempBuffer);
                String dataJson = "{\"common\":{\"app_id\":\"" + appId + "\"},\"business\":{\"domain\":\"iat\",\"language\":\"zh_cn\",\"accent\":\"mandarin\",\"vad_eos\":10000},\"data\":{\"status\":1,\"format\":\"audio/L16;rate=16000\",\"encoding\":\"raw\",\"audio\":\"" + audioBase64 + "\"}}";
                client.send(dataJson);
                Thread.sleep(40); // 控制发送间隔
            }

            // 发送结束帧
            String endJson = "{\"common\":{\"app_id\":\"" + appId + "\"},\"business\":{\"domain\":\"iat\",\"language\":\"zh_cn\",\"accent\":\"mandarin\",\"vad_eos\":10000},\"data\":{\"status\":2,\"format\":\"audio/L16;rate=16000\",\"encoding\":\"raw\",\"audio\":\"\"}}";
            client.send(endJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getServerTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", java.util.Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(new Date());
    }

    private String generateSignature(String date) throws Exception {
        String signatureOrigin = "host: iat-api.xfyun.cn\ndate: " + date + "\nGET /v2/iat HTTP/1.1";
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] signatureBytes = mac.doFinal(signatureOrigin.getBytes(StandardCharsets.UTF_8));
        String signature = Base64.getEncoder().encodeToString(signatureBytes);

        String authorizationOrigin = "api_key=\"" + apiKey + "\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"" + signature + "\"";
        String authorization = Base64.getEncoder().encodeToString(authorizationOrigin.getBytes(StandardCharsets.UTF_8));

        return URLEncoder.encode(authorization, StandardCharsets.UTF_8.toString());
    }
}