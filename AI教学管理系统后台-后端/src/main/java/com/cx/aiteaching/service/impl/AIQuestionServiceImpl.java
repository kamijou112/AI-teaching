package com.cx.aiteaching.service.impl;

import com.cx.aiteaching.entity.Question;
import com.cx.aiteaching.service.AIQuestionService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AIQuestionServiceImpl implements AIQuestionService {

    private static final String API_URL = "https://spark-api-open.xf-yun.com/v1/chat/completions";
    private static final String API_SECRET = "YjBjNDAyMmE3NDc0N2U3Y2EyNzI5NmZj";
    private static final String API_KEY = "dce851f412509fca0f3188e488f3ba09";

    private final OkHttpClient client;
    private final Gson gson;

    public AIQuestionServiceImpl() {
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS) // 设置连接超时时间
                .readTimeout(50, TimeUnit.SECONDS) // 设置读取超时时间
                .writeTimeout(50, TimeUnit.SECONDS) // 设置写入超时时间
                .build();;
        this.gson = new Gson();
    }

    @Override
    public List<Question> generateQuestions(String requirements) throws IOException {
        // 构建请求体
        String jsonRequest = buildJsonRequest(requirements);

        // 创建请求
        RequestBody body = RequestBody.create(jsonRequest, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY + ":" + API_SECRET)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        // 发送请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // 获取响应体
            String responseBody = response.body().string();
            // 打印原始响应数据
            System.out.println("原始响应数据: " + responseBody);

            // 解析响应体
            return parseResponse(responseBody);
        }
    }

    private String buildJsonRequest(String requirements) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("model", "generalv3.5");
        JsonArray messagesArray = new JsonArray();
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("role", "user");
        messageObject.addProperty("content", "请根据以下要求生成题目："
                + requirements
                + "以这个格式回答["
                + "{"
                + "\"type\": \"number\", // Type of the question (1 for fill-in-the-blank, 2 for multiple-choice, 3 for true/false)"
                + "\"tigan\": \"string\", // Question content"
                + "\"xuanxiang\": \"string\", // Options for multiple-choice questions, separated by '|', 如果不是选择题，这个就等于\"\""
                + "\"daan\": \"string\" // 如果type=1，那么答案等于填空题答案；如果type=2，那么答案等于\"1\"或者\"2\"或者\"3\"或者\"4\"，分别对应选项A、B、C、D；如果type=3，答案等于1或者2，分别对应对、错。"
                + "}] // 后面的内容不需要输出，这句话也不要”"
                + "角色设定为“你是一个出题助手”");
        messagesArray.add(messageObject);
        jsonObject.add("messages", messagesArray);
        return jsonObject.toString();
    }

    private List<Question> parseResponse(String responseBody) {
        List<Question> questions = new ArrayList<>();
        JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
        JsonArray choicesArray = jsonResponse.getAsJsonArray("choices");
        for (JsonElement choiceElement : choicesArray) {
            JsonObject choiceObject = choiceElement.getAsJsonObject();
            JsonObject messageObject = choiceObject.getAsJsonObject("message");
            String content = messageObject.get("content").getAsString();
            // 假设content是题目列表的JSON字符串，解析它
            JsonArray questionArray = gson.fromJson(content, JsonArray.class);
            for (JsonElement questionElement : questionArray) {
                questions.add(gson.fromJson(questionElement, Question.class));
            }
        }
        return questions;
    }
}
