package com.cx.aischeduledtasks.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cx.aischeduledtasks.entity.CourseTasks;
import com.cx.aischeduledtasks.entity.TaskLog;
import com.cx.aischeduledtasks.entity.Topic;
import com.cx.aischeduledtasks.entity.TopicDetails;
import com.cx.aischeduledtasks.mapper.CourseTasksMapper;
import com.cx.aischeduledtasks.mapper.TaskLogMapper;
import com.cx.aischeduledtasks.mapper.TopicDetailsMapper;
import com.cx.aischeduledtasks.mapper.TopicMapper;
import com.cx.aischeduledtasks.service.TaskLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/25 16:38
 */

@Component
public class TaskUtil {

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


    // 添加定时任务
    @Scheduled(cron = "*/2 * * * * *")
    public void doTask() throws IOException {
        TaskLog log = taskLogService.log();
        if (log == null) {
            System.out.println("没有匹配的任务~");
            return;
        }
        QueryWrapper<TopicDetails> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tasklogid",log.getId());
        List<TopicDetails> topicDetails = topicDetailsMapper.selectList(queryWrapper);
        for (TopicDetails topicDetail : topicDetails) {
            if (topicDetail.getIsdui() != 0) {
                continue;
            }
            Topic topic = topicMapper.selectById(topicDetail.getTimuid());
            if (topic.getDaan().equals(topicDetail.getZuoda())) {
                topicDetail.setIsdui(1);
                topicDetailsMapper.updateById(topicDetail);
            }else {
                String json = kdWebHttpUtil.json(topic.getTigan(), topic.getType(),topic.getXuanxiang(), topic.getDaan(), topicDetail.getZuoda());
                String s = kdWebHttpUtil.sendPostRequest(json);
                System.out.println(s);
                // 使用 Gson 对象将 JSON 字符串转换为 Person 对象
                Gson gson = new Gson();
                JsonObject jsonObject = gson.fromJson(s, JsonObject.class);
                JsonArray choices = jsonObject.getAsJsonArray("choices");
                JsonObject choice = choices.get(0).getAsJsonObject();
                JsonObject message = choice.getAsJsonObject("message");
                String content1 = message.get("content").getAsString();
                topicDetail.setIsdui(2);
                topicDetail.setAijiexi(content1);
                topicDetailsMapper.updateById(topicDetail);
            }
        }
        log.setStatus(3);
        taskLogMapper.updateById(log);
        CourseTasks courseTasks = courseTasksMapper.selectById(log.getTaskid());
        courseTasks.setWanchengsounts(courseTasks.getWanchengsounts() + 1);
        courseTasksMapper.updateById(courseTasks);
        System.out.println("定时任务了一次~");
    }




}
