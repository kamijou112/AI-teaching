package com.cx.aiteaching.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aiteaching.entity.Course;
import com.cx.aiteaching.mapper.CourseMapper;
import com.cx.aiteaching.service.CourseService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 13:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Resource
    private CourseMapper courseMapper;

    @Override
    public List<Course> getCourse(String wordkey) {
        return courseMapper.getCourse(wordkey);
    }
}
