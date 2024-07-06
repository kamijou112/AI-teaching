package com.cx.aischeduledtasks.vo;

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

    private String msg;

    private Object data;

}
