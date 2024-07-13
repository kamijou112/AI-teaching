package com.cx.aischeduledtasks.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cx.aischeduledtasks.entity.*;
import com.cx.aischeduledtasks.mapper.*;
import com.cx.aischeduledtasks.service.StudentService;
import com.cx.aischeduledtasks.service.TaskLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.annotation.Resource;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/7/2 14:29
 */
@Component
public class StudentResult {
    @Resource
    private TaskLogMapper taskLogMapper;
    @Resource
    private TaskLogService taskLogService;
    @Resource
    private TopicDetailsMapper topicDetailsMapper;
    @Resource
    private TopicMapper topicMapper;
    @Resource
    private KDWebHttpUtil kdWebHttpUtil;
    @Resource
    private CourseTasksMapper courseTasksMapper;
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private StudentService studentService;

    // 添加定时任务
    @Scheduled(cron = "*/2 * * * * *")
    public void doTask() throws IOException {
        Integer studentId = TaskUtil.getLastStudentId();
        if (studentId == null) {
            System.out.println("没有匹配的任务~");
            return;
        }

        // 使用后清空 studentId
        TaskUtil.clearLastStudentId();

        Student student = studentMapper.selectById(studentId);
        if (student == null) {
            System.out.println("没有找到对应的学生~");
            return;
        }

        QueryWrapper<TaskLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 3);
        queryWrapper.eq("studentid", student.getId());
        List<TaskLog> taskLogs = taskLogMapper.selectList(queryWrapper);
        if (taskLogs == null || taskLogs.isEmpty()) {
            System.out.println("该学生没有任务~");
            return;
        }

        List<TopicDetails> list = new ArrayList<>();
        for (TaskLog taskLog : taskLogs) {
            QueryWrapper<TopicDetails> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("tasklogid", taskLog.getId());
            queryWrapper1.eq("isdui", 2);
            queryWrapper1.orderByDesc("createtime"); // 按创建时间倒序排列
            queryWrapper1.last("LIMIT 10"); // 限制最多取10条记录
            List<TopicDetails> topicDetails = topicDetailsMapper.selectList(queryWrapper1);
            if (topicDetails == null || topicDetails.isEmpty()) {
                continue;
            }
            list.addAll(topicDetails);
        }

        if (list.isEmpty()) {
            System.out.println("没有找到该学生的错题解析~");
            return;
        }

        String aijiexi = "";
        for (TopicDetails topicDetails : list) {
            aijiexi += topicDetails.getAijiexi() + ",";
        }



        AiJosn myJsonObject = new AiJosn();
        myJsonObject.setModel("generalv3.5");

        List<Message> messages = new ArrayList<>();

        Message systemMessage = new Message();
        systemMessage.setRole("system");
        systemMessage.setContent("你是一个高级教师，精通所有学科，你有着严谨开放的精神，你善于对学生的知识薄弱点进行分析,对学生学习成果进行评估,善于对学生的错题进行解析并给出解题思路，善于用温和的语言为学生指出问题并给出专业的解析");
        messages.add(systemMessage);

        Message userMessage = new Message();
        userMessage.setRole("user");
        userMessage.setContent("以下是学生最近10道错题的解析，请你分析学生最近学习成果。" + aijiexi+
                "你需要按照下面这种格式回复我:【易错知识点】[此处是从解析提取的易错知识点，然后换行]"+
                "【错误类型分析】[此处区分学生的错误类型，例如概念性错误、计算错误、粗心大意、审题不清等，了解错误的根本原因，然后换行]"+
                "【重复错误点】[此处判断学生是否在同一类型的题目上反复出错，评估他们是否对某些知识点存在持续的理解障碍，如果没有则为“你没有重复的错误知识点”。]"+
                "【反思与改进】[此处填写对错题的反思与纠正，然后换行]");
        messages.add(userMessage);
        System.out.println(userMessage);
        myJsonObject.setMessages(messages);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(myJsonObject);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS) // 设置连接超时时间
                .readTimeout(60, TimeUnit.SECONDS) // 设置读取超时时间
                .writeTimeout(60, TimeUnit.SECONDS) // 设置写入超时时间
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url("https://spark-api-open.xf-yun.com/v1/chat/completions")
                .method("POST", body)
                .addHeader("Authorization", "Bearer 7399fbfee518a47fa4b19987fe0c4f6b:MWVkM2MwMjliMzE5MmY4M2EzM2YxZGY2")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        String responseData = response.body().string();

        System.out.println(responseData);

        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
        JsonArray choices = jsonObject.getAsJsonArray("choices");
        JsonObject choice = choices.get(0).getAsJsonObject();
        JsonObject message = choice.getAsJsonObject("message");
        String content1 = message.get("content").getAsString();

        student.setStudyresult(content1);
        studentMapper.updateById(student);

        System.out.println("定时任务了一次。。。。。。");
    }
}
