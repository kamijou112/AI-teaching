package com.cx.aischeduledtasks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aischeduledtasks.entity.CourseTasks;
import com.cx.aischeduledtasks.mapper.CourseTasksMapper;
import com.cx.aischeduledtasks.service.CourseTasksService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 14:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CourseTasksServiceImpl extends ServiceImpl<CourseTasksMapper, CourseTasks> implements CourseTasksService {
    @Resource
    private CourseTasksMapper courseTasksMapper;

}
