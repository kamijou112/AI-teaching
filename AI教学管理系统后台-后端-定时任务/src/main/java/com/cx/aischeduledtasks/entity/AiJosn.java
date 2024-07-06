package com.cx.aischeduledtasks.entity;

import lombok.Data;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/25 18:41
 */
@Data
public class AiJosn {
    private String model;
    private List<Message> messages;
}
