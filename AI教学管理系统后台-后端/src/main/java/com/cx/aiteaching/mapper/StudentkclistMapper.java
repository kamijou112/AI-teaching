package com.cx.aiteaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cx.aiteaching.entity.Studentkclist;
import com.cx.aiteaching.vo.CourseStudentVo;
import com.cx.aiteaching.vo.StudentkclistVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 17:25
 */
@Mapper
public interface StudentkclistMapper extends BaseMapper<Studentkclist> {

    @Select("SELECT a.*,b.kcname FROM `ai_studentkclist` as a LEFT JOIN ai_kecheng as b ON a.kcid = b.id WHERE a.studentid = #{id}")
    List<StudentkclistVo> getCourseListByStudent(Integer id);

    @Select("SELECT  a.id,a.studentid,a.kcid,b.name,a.createtime,c.kcname  FROM `ai_studentkclist` AS a LEFT JOIN ai_students as b ON a.studentid = b.id LEFT JOIN `ai_kecheng` as c ON a.kcid = c.id WHERE a.kcid = #{courseid}")
    List<CourseStudentVo> getCourseStudent(Integer courseid);

}
