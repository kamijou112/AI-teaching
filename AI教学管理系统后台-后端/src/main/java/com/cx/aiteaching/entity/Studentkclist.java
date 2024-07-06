package com.cx.aiteaching.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 17:14
 */
@Data
@TableName("ai_studentkclist")
public class Studentkclist implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//主键id
    @TableField("studentid")
    private Integer studentid;//所属学生id
    @TableField("kcid")
    private Integer kcid;//加入的课程id
    @TableField("createtime")
    private Long createtime;//加入时间

}
