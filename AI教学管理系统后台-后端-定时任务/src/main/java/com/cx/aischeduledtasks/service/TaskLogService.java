package com.cx.aischeduledtasks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cx.aischeduledtasks.entity.TaskLog;


/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 18:21
 */
public interface TaskLogService extends IService<TaskLog> {


    TaskLog log();
}
