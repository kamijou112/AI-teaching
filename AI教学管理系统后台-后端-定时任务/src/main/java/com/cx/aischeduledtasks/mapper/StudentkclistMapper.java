package com.cx.aischeduledtasks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.cx.aischeduledtasks.entity.Studentkclist;
import com.cx.aischeduledtasks.vo.StudentkclistVo;
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

}
