package com.cx.aiteaching.mapper;

import com.cx.aiteaching.dto.StudentWrongQuestionsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {

    @Select("SELECT t.tigan AS question, " +
            "       CASE t.type " +
            "           WHEN 1 THEN td.zuoda " +  // 填空题，直接返回学生答案
            "           WHEN 2 THEN (SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(t.xuanxiang, '|', td.zuoda), '|', -1)) " +  // 选择题，转换为选项内容
            "           WHEN 3 THEN (CASE td.zuoda WHEN '1' THEN '正确' ELSE '错误' END) " +  // 判断题，转换为对错
            "           ELSE td.zuoda " +
            "       END AS studentAnswer, " +
            "       CASE t.type " +
            "           WHEN 1 THEN t.daan " +  // 填空题，直接返回正确答案
            "           WHEN 2 THEN (SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(t.xuanxiang, '|', t.daan), '|', -1)) " +  // 选择题，转换为选项内容
            "           WHEN 3 THEN (CASE t.daan WHEN '1' THEN '正确' ELSE '错误' END) " +  // 判断题，转换为对错
            "           ELSE t.daan " +
            "       END AS correctAnswer, " +
            "       td.aijiexi AS analysis " +
            "FROM ai_taskdolog td " +
            "JOIN ai_tasklog tl ON td.tasklogid = tl.id " +
            "JOIN ai_timu t ON td.timuid = t.id " +
            "WHERE tl.studentid = #{studentId} AND td.isdui = 2")
    List<StudentWrongQuestionsDTO> getStudentWrongQuestions(@Param("studentId") int studentId);
}
