package com.cx.aiteaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cx.aiteaching.entity.Course;
import com.cx.aiteaching.mapper.CourseMapper;
import jakarta.annotation.Resource;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 13:12
 */
public interface CourseService extends IService<Course> {



    List<Course> getCourse(String wordkey);
}
