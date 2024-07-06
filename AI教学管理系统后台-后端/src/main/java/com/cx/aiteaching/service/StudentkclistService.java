package com.cx.aiteaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cx.aiteaching.entity.Studentkclist;
import com.cx.aiteaching.vo.CourseStudentVo;
import com.cx.aiteaching.vo.StudentkclistVo;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 17:26
 */
public interface StudentkclistService extends IService<Studentkclist> {

    List<StudentkclistVo> getCourseListByStudent(Integer id);

    List<CourseStudentVo> getCourseStudent(Integer courseid);
}
