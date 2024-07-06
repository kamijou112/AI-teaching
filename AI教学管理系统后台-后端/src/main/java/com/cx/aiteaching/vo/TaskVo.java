package com.cx.aiteaching.vo;

import lombok.Data;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 19:37
 */
@Data
public class TaskVo {

    private Integer taskid;

    private Integer status;

    private String taskname;

    private String msg;

    private Integer type;

    private Object data;

    private Long datetime;

}
