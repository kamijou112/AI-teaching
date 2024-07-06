package com.cx.aiteaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cx.aiteaching.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 13:11
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    @Select(" SELECT * FROM ai_kecheng WHERE  id = #{wordkey} OR INSTR(kcname ,#{wordkey})")
    List<Course> getCourse(String wordkey);
}
