package com.cx.aischeduledtasks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cx.aischeduledtasks.entity.Student;


/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 11:57
 */
public interface StudentService extends IService<Student> {

    Student getStudent();
}
