package com.cx.aischeduledtasks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aischeduledtasks.entity.TaskLog;
import com.cx.aischeduledtasks.mapper.TaskLogMapper;
import com.cx.aischeduledtasks.service.TaskLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 18:21
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog> implements TaskLogService {

    @Resource
    private TaskLogMapper taskLogMapper;
    @Override
    public TaskLog log() {
        return taskLogMapper.log();
    }
}
