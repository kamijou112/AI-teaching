package com.cx.aiteaching.service.impl;

import com.cx.aiteaching.service.SpeechRecognitionService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

@Service
public class SpeechRecognitionServiceImpl implements SpeechRecognitionService {
    private static final String HOST_URL = "https://iat-api.xfyun.cn/v2/iat";
    private static final String APP_ID = "1e10f19e";
    private static final String API_SECRET = "YjBjNDAyMmE3NDc0N2U3Y2EyNzI5NmZj";
    private static final String API_KEY = "dce851f412509fca0f3188e488f3ba09";
    private static final Gson GSON = new Gson();
    private CountDownLatch latch;
    private String resultText;

    @Override
    public String recognizeSpeech(byte[] audioData) {
        try {
            String authUrl = getAuthUrl(HOST_URL, API_KEY, API_SECRET);
            OkHttpClient client = new OkHttpClient.Builder().build();
            String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");

            Request request = new Request.Builder().url(url).build();
            latch = new CountDownLatch(1); // Initialize CountDownLatch

            WebSocket webSocket = client.newWebSocket(request, new WebSocketListenerImpl(audioData));

            latch.await(); // Wait for the WebSocket connection to process and return the result

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultText; // Return the recognized text
    }

    private static String getAuthUrl(String hostUrl, String apiKey, String apiSecret) throws Exception {
        URL url = new URL(hostUrl);
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = format.format(new Date());
        StringBuilder builder = new StringBuilder("host: ").append(url.getHost()).append("\n")
                .append("date: ").append(date).append("\n")
                .append("GET ").append(url.getPath()).append(" HTTP/1.1");

        Charset charset = Charset.forName("UTF-8");
        Mac mac = Mac.getInstance("hmacsha256");
        SecretKeySpec spec = new SecretKeySpec(apiSecret.getBytes(charset), "hmacsha256");
        mac.init(spec);
        byte[] hexDigits = mac.doFinal(builder.toString().getBytes(charset));
        String sha = Base64.getEncoder().encodeToString(hexDigits);

        String authorization = String.format("api_key=\"%s\", algorithm=\"%s\", headers=\"%s\", signature=\"%s\"",
                apiKey, "hmac-sha256", "host date request-line", sha);
        return new URL("https://" + url.getHost() + url.getPath()).toString() +
                "?authorization=" + Base64.getEncoder().encodeToString(authorization.getBytes(charset)) +
                "&date=" + date + "&host=" + url.getHost();
    }

    private class WebSocketListenerImpl extends WebSocketListener {
        private final byte[] audioData;

        public WebSocketListenerImpl(byte[] audioData) {
            this.audioData = audioData;
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            new Thread(() -> {
                try {
                    sendAudioData(webSocket, audioData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        private void sendAudioData(WebSocket webSocket, byte[] audioData) throws InterruptedException, IOException {
            int frameSize = 1280;
            int interval = 40;
            int status = 0;
            ByteArrayInputStream audioStream = new ByteArrayInputStream(audioData);
            byte[] buffer = new byte[frameSize];
            int len;

            while ((len = audioStream.read(buffer)) != -1) {
                byte[] actualBuffer = new byte[len];
                System.arraycopy(buffer, 0, actualBuffer, 0, len);

                JsonObject frame = new JsonObject();
                JsonObject business = new JsonObject();
                JsonObject common = new JsonObject();
                JsonObject data = new JsonObject();

                common.addProperty("app_id", APP_ID);
                business.addProperty("language", "zh_cn");
                business.addProperty("domain", "iat");
                business.addProperty("accent", "mandarin");
                data.addProperty("status", status);
                data.addProperty("format", "audio/L16;rate=16000");
                data.addProperty("encoding", "raw");
                data.addProperty("audio", Base64.getEncoder().encodeToString(actualBuffer));

                frame.add("common", common);
                frame.add("business", business);
                frame.add("data", data);

                webSocket.send(frame.toString());

                status = 1;  // set status to 'continue'
                Thread.sleep(interval);
            }

            // Send last frame
            JsonObject frame = new JsonObject();
            JsonObject data = new JsonObject();
            data.addProperty("status", 2);
            data.addProperty("format", "audio/L16;rate=16000");
            data.addProperty("encoding", "raw");
            data.addProperty("audio", "");
            frame.add("data", data);
            webSocket.send(frame.toString());
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            System.out.println(text);
            JsonObject jsonResponse = GSON.fromJson(text, JsonObject.class);
            JsonObject data = jsonResponse.getAsJsonObject("data");
            if (data != null) {
                JsonObject result = data.getAsJsonObject("result");
                if (result != null) {
                    JsonArray wsArray = result.getAsJsonArray("ws");
                    if (wsArray != null) {
                        StringBuilder recognizedText = new StringBuilder();
                        for (int i = 0; i < wsArray.size(); i++) {
                            JsonObject wsItem = wsArray.get(i).getAsJsonObject();
                            JsonArray cwArray = wsItem.getAsJsonArray("cw");
                            if (cwArray != null && cwArray.size() > 0) {
                                JsonObject cwItem = cwArray.get(0).getAsJsonObject();
                                String word = cwItem.get("w").getAsString();
                                recognizedText.append(word);
                            }
                        }
                        resultText = recognizedText.toString();
                        latch.countDown(); // Signal that the result has been received
                    }
                }
            }
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
            if (response != null) {
                try {
                    System.out.println("Error: " + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            latch.countDown(); // Ensure the latch is decremented even if there's a failure
        }
    }
}
