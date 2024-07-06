package com.cx.aischeduledtasks.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cx.aischeduledtasks.entity.AiJosn;
import com.cx.aischeduledtasks.entity.Message;
import com.cx.aischeduledtasks.properties.KdWebProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import okhttp3.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/25 18:02
 */
@Configuration
public class KDWebHttpUtil {

    @Resource
    private KdWebProperties kdWebProperties;

    //https://spark-api-open.xf-yun.com/v1/chat/completions
    public final static String Url = "https://spark-api-open.xf-yun.com/v1/chat/completions";


    public String json(String topic, int type,String options, String cAnswer, String stuAnswer) throws JsonProcessingException {
        AiJosn myJsonObject = new AiJosn();
        myJsonObject.setModel("generalv3.5");

        List<Message> messages = new ArrayList<>();

        Message systemMessage = new Message();
        systemMessage.setRole("system");
        systemMessage.setContent("你是一个高级教师，精通所有学科，你有着严谨开放的精神，你善于对学生的知识薄弱点进行分析,对学生学习成果进行评估,善于对学生的错题进行解析并给出解题思路，善于用温和的语言为学生指出问题并给出专业的解析");
        messages.add(systemMessage);

        Message userMessage = new Message();
        userMessage.setRole("user");
        String content = content(topic, type,options, cAnswer, stuAnswer);
        userMessage.setContent(content);
        messages.add(userMessage);

        myJsonObject.setMessages(messages);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(myJsonObject);

//        System.out.println("=======================================================");
//        System.out.println(myJsonObject);
//        System.out.println("=======================================================");

        return json;
    }

    public String sendPostRequest(String requestBody) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间
                .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时时间
                .writeTimeout(30, TimeUnit.SECONDS) // 设置写入超时时间
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody);
        Request request = new Request.Builder()
                .url(Url)
                .method("POST", body)
                .addHeader("Authorization", "Bearer 786a5dced09ec88faee20ad1967c5c6f:NzU4NDgwOWIxZjEwZjg2ZThjMGMxZjRj")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();

        return responseData;
    }


    public String content(String topic, int type,String options, String cAnswer, String stuAnswer) {

        if(type == 2){
            String con = "你可以帮助学生分析错题原因以及需要注意的事项等，我会告诉你题干【题目】还有正确答案【正确答案】和学生回答的错误答案【学生作答】，如果是选择题则还有选项【选项】，你需要分析学生的错误原因，并给出错误分析然后告诉学生错在哪，以后应该注意什么。" +
                    "下面是本次需要为学生解惑的错题【题目】" + topic +  "【正确答案】" + cAnswer(cAnswer) + "【学生作答】" + cAnswer(stuAnswer) + "【选项】" + options(options) +"，你需要按照这种下面格式回复我:【解析】[此处是题目的解析，然后换行]【错误点】[此处是答题的错误点，开头为“你的答案是...”，最后换行]【指导建议】[此处是对本道错题的指导建议]，要注意的是回复我的时候你要把我当成你的学生，对待称呼你要统一用“你”，这样可以让学生感到亲切,";
            return con;
        }
        else if(type == 1){
            String con = "你可以帮助学生分析错题原因以及需要注意的事项等，我会告诉你题干【题目】还有正确答案【正确答案】和学生回答的错误答案【学生作答】，你需要分析学生的错误原因，并给出错误分析然后告诉学生错在哪，以后应该注意什么。" +
                    "下面是本次需要为学生解惑的错题【题目】" + topic +  "【正确答案】" + cAnswer + "【学生作答】" + stuAnswer +"，你需要按照这种下面格式回复我:【解析】[此处是题目的解析，然后换行]【错误点】[此处是答题的错误点，开头为“你的答案是...”，最后换行]【指导建议】[此处是对本道错题的指导建议]，要注意的是回复我的时候你要把我当成你的学生，对待称呼你要统一用“你”，这样可以让学生感到亲切,";
            return con;
        }
        //判断题
        else {
            String con = "你可以帮助学生分析错题原因以及需要注意的事项等，我会告诉你题干【题目】还有正确答案【正确答案】和学生回答的错误答案【学生作答】，你需要分析学生的错误原因，并给出错误分析然后告诉学生错在哪，以后应该注意什么。" +
                    "下面是本次需要为学生解惑的判断题【题目】" + topic +  "【正确答案】" + cAnswer2(cAnswer) + "【学生作答】" + cAnswer2(stuAnswer) +"，你需要按照这种下面格式回复我:【解析】[此处是题目的解析，然后换行]【错误点】[此处是答题的错误点，开头为“你的答案是...”，最后换行]【指导建议】[此处是对本道错题的指导建议]，要注意的是回复我的时候你要把我当成你的学生，对待称呼你要统一用“你”，这样可以让学生感到亲切,";
            return con;
        }



    }


    public String options(String options) {

        if (options == null) {
            return "";
        }
        String[] commands = options.split("\\|");                     //分割选项
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < commands.length; i++) {
            result.append((char) ('A' + i)).append('.').append(commands[i]);
        }
        return result.toString();
    }


    public String cAnswer(String cAnswer) {
        if (cAnswer == null) return null;
        else  if (cAnswer.equals("1")) return "A";
        else if (cAnswer.equals("2")) return "B";
        else if (cAnswer.equals("3")) return "C";
        else if (cAnswer.equals("4")) return "D";
        return cAnswer;
    }

    public String cAnswer2(String cAnswer) {
        if (cAnswer == null) return null;
        else  if (cAnswer.equals("1")) return "正确";
        else if (cAnswer.equals("2")) return "错误";

        return cAnswer;
    }

//    public static void main(String[] args) throws IOException {
//
//
//        AiJosn myJsonObject = new AiJosn();
//        myJsonObject.setModel("generalv3.5");
//
//        List<Message> messages = new ArrayList<>();
//
//        Message systemMessage = new Message();
//        systemMessage.setRole("system");
//        systemMessage.setContent("你是一个高级教师，精通所有学科，你有着严谨开放的精神，你善于对学生的知识薄弱点进行分析,对学生学习成果进行评估,善于对学生的错题进行解析并给出解题思路，善于用温和的语言为学生指出问题并给出专业的解析");
//        messages.add(systemMessage);
//
//        Message userMessage = new Message();
//        userMessage.setRole("user");
//        userMessage.setContent("你可以帮助学生分析错题原因以及需要注意的事项等，我会告诉你题干【题目】和选项（如果是选择题）【选项】还有正确答案【正确选项】和学生回答的错误答案【学生作答】，你需要分析学生的错误原因，并给出错误分析然后告诉学生错在哪，以后应该注意什么。下面是本次需要为学生解惑的错题“【题目】The problem ____ when the students refused to do their homework.【选项】A.arose B.aroused C.rose D.raised【正确选项】A【学生作答】B”，你需要按照这种下面格式回复我“【解析】[此处是题目的解析，然后换行]【错误点】[此处是答题的错误点，然后换行]【指导建议】[此处是对本道错题的指导建议]”，要注意的是回复我的时候你要把我当成你的学生，对待称呼你要统一用“你”，这样可以让学生感到亲切");
//        messages.add(userMessage);
//
//        myJsonObject.setMessages(messages);
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(myJsonObject);
//
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间
//                .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时时间
//                .writeTimeout(30, TimeUnit.SECONDS) // 设置写入超时时间
//                .build();
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, json);
//        Request request = new Request.Builder()
//                .url("https://spark-api-open.xf-yun.com/v1/chat/completions")
//                .method("POST", body)
//                .addHeader("Authorization", "Bearer 7399fbfee518a47fa4b19987fe0c4f6b:MWVkM2MwMjliMzE5MmY4M2EzM2YxZGY2")
//                .addHeader("Content-Type", "application/json")
//                .build();
//        Response response = client.newCall(request).execute();
//        String responseData = response.body().string();
//
//        System.out.println(responseData);
//
//
//    }

}
