package com.cx.aischeduledtasks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cx.aischeduledtasks.entity.Studentkclist;
import com.cx.aischeduledtasks.vo.StudentkclistVo;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 17:26
 */
public interface StudentkclistService extends IService<Studentkclist> {

    List<StudentkclistVo> getCourseListByStudent(Integer id);
}
