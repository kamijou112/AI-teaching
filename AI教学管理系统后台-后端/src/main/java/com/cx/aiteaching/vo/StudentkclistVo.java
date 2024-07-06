package com.cx.aiteaching.vo;

import lombok.Data;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 17:32
 */
@Data
public class StudentkclistVo {

    private Integer id;//主键id

    private Integer studentid;//所属学生id

    private Integer kcid;//加入的课程id

    private Long createtime;//加入时间

    private String kcname;
}
