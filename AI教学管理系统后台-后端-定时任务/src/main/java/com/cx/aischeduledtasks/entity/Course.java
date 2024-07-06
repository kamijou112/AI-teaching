package com.cx.aischeduledtasks.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 13:07
 */
@Data
@TableName("ai_kecheng")
public class Course  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//课程id
    @TableField("kcname")
    private String kcname;//课程名称
    @TableField("studentcounts")
    private Integer studentcounts;//加入学生人数
    @TableField("createtime")
    private Long createtime;//课程创建时间

}
