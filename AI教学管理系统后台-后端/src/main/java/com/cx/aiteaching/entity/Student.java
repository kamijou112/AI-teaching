package com.cx.aiteaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**

 @author 汪洋

 @version 1.0

 @date 2024/6/24 11:52
 */
@Data
@TableName("ai_students")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//学生id
    @TableField("name")
    private String name;// 学生姓名
    @TableField("username")
    private String username;//学生账号
    @TableField("password")
    private String password;//学生密码
    @TableField("sex")
    private Integer sex;// 学生性别:1=男,2=女
    @TableField("status")
    private Integer status;//状态:0=正常,1=封禁
    @TableField("studyresult")
    private String studyresult;//学生近期学习成果
    @TableField("createtime")
    private Long createtime;//创建时间

}