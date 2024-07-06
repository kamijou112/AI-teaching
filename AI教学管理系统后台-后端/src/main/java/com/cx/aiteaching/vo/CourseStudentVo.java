package com.cx.aiteaching.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/7/1 10:26
 */
@Data
public class CourseStudentVo {
    private Integer id;//主键id
    private Integer studentid;//所属学生id
    private Integer kcid;//加入的课程id
    private Long createtime;//加入时间
    private String name;//	学生姓名
    private String kcname;//课程名称
}
