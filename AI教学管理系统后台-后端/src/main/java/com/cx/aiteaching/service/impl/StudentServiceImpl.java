package com.cx.aiteaching.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aiteaching.entity.Student;
import com.cx.aiteaching.mapper.StudentMapper;
import com.cx.aiteaching.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 11:59
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Resource
    private StudentMapper studentMapper;

}
