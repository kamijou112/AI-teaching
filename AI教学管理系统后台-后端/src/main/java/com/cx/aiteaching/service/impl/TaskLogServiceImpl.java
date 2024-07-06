package com.cx.aiteaching.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aiteaching.entity.TaskLog;
import com.cx.aiteaching.mapper.TaskLogMapper;
import com.cx.aiteaching.service.TaskLogService;
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
}
