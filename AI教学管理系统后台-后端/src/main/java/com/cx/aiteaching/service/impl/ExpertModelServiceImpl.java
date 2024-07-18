package com.cx.aiteaching.service.impl;

import com.cx.aiteaching.service.ExpertModelService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ExpertModelServiceImpl implements ExpertModelService {

    private static final String API_URL = "https://spark-api-open.xf-yun.com/v1/chat/completions";
    private static final String API_SECRET = "YmQxNmQyOTA1MDU4YjIxODA1NmUzZDYx";
    private static final String API_KEY = "b7c44ecfd5a60cc1de69ac7c996e8c25";

    private final OkHttpClient client;

    public ExpertModelServiceImpl() {
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String ExpertModeloutput(String studentResult, String type) throws IOException {
        // 构建请求体
        String jsonRequest = buildJsonRequest(studentResult, type);

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
            System.out.println("原始响应数据: " + responseBody);

            // 解析响应体
            return parseResponse(responseBody);
        }
    }

    private String buildJsonRequest(String studentResult, String type) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("model", "generalv3.5");

        JsonArray messagesArray = new JsonArray();
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("role", "user");

        String content = getContentByType(type, studentResult);
        messageObject.addProperty("content", content);

        messagesArray.add(messageObject);
        jsonObject.add("messages", messagesArray);
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }

    private String getContentByType(String type, String studentResult) {
        switch (type) {
            case "books":
                return "根据学习成果内容中的不足，进行在线搜索，为我推荐三本书籍，你需要按照下面格式回复我{\"recommend\":\" 【书名】:【ISBN】;【书名】:【ISBN】\"}" +
                        "每本书之间用分号隔开，当中不要出现【书名】:【ISBN】，以下是我的学习成果:" + studentResult;
            case "websites":
                return "根据学习成果内容中的不足，进行在线搜索，为我推荐三个网站，你需要按照下面格式回复我{\"recommend\":\" 【网页标题】:【网址】;【网页标题】:【网址】\"}" +
                        "每个网站之间用分号隔开，当中不要出现【网页标题】:【网址】，以下是我的学习成果:" + studentResult;
            case "courses":
                return "根据学习成果内容中的不足，在bilibili(https://www.bilibili.com/)和中国大学MOOC(https://www.icourse163.org/)上进行在线搜索，为我推荐三个在线课程，你需要按照下面格式回复我{\"recommend\":\" 【课程名】【课程链接】;【课程名】【课程链接】\"}" +
                        "每个课程之间用分号隔开，当中不要出现【课程名】，以下是我的学习成果:" + studentResult;
            case"papers":
                return "根据学习成果内容中的不足，进行在线搜索，为我推荐相关的三篇英文论文，你需要按照下面格式回复我{\"recommend\":\" 【论文名】【论文作者】【期刊名称】【文献类型】;【论文名】【论文作者】【期刊名称】【文献类型】\"}" +
                        "每篇论文之间用分号隔开，当中不要出现【论文名】【论文作者】【期刊名称】【文献类型】，以下是我的学习成果:" + studentResult;
            default:
                return "未知的类型，请输入有效的类型【问题】" + studentResult;
        }
    }

    private String parseResponse(String responseBody) {
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray choices = jsonObject.getAsJsonArray("choices");
        if (choices.size() > 0) {
            JsonObject firstChoice = choices.get(0).getAsJsonObject();
            JsonObject message = firstChoice.getAsJsonObject("message");
            return message.get("content").getAsString();
        }
        return null;
    }
}
