package com.cx.aischeduledtasks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cx.aischeduledtasks.entity.TaskLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 18:18
 */
@Mapper
public interface TaskLogMapper extends BaseMapper<TaskLog> {
    @Select("SELECT * FROM `ai_tasklog` WHERE status = 2 LIMIT 1")
    TaskLog log();
}
