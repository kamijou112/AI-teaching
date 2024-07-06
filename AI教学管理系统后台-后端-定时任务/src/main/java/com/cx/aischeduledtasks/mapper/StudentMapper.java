package com.cx.aischeduledtasks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cx.aischeduledtasks.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 11:56
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select("select  * from ai_students where studyresult is null limit  1")
    Student getStudent();
}
