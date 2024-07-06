package com.cx.aiteaching.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aiteaching.entity.Studentkclist;
import com.cx.aiteaching.mapper.StudentkclistMapper;
import com.cx.aiteaching.service.StudentkclistService;
import com.cx.aiteaching.vo.CourseStudentVo;
import com.cx.aiteaching.vo.StudentkclistVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 17:26
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class StudentkclistServiceImpl extends ServiceImpl<StudentkclistMapper, Studentkclist> implements StudentkclistService {
    @Resource
    private  StudentkclistMapper studentkclistMapper;


    @Override
    public List<StudentkclistVo> getCourseListByStudent(Integer id) {
        return studentkclistMapper.getCourseListByStudent(id);
    }

    @Override
    public List<CourseStudentVo> getCourseStudent(Integer courseid) {
        return studentkclistMapper.getCourseStudent(courseid);
    }
}
