package com.cx.aiteaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cx.aiteaching.entity.Course;
import com.cx.aiteaching.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 11:56
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {


    @Select(" SELECT studyresult FROM ai_students WHERE  username = #{usernmae} ")
    String selectByUsername(String username);
}
