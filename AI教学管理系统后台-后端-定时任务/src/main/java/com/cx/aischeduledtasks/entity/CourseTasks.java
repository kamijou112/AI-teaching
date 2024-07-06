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
 * @date 2024/6/24 14:09
 */
@Data
@TableName("ai_task")
public class CourseTasks implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//任务id
    @TableField("taskname")
    private String taskname;//任务名称
    @TableField("wanchengsounts")
    private Integer wanchengsounts;//	完成人数
    @TableField("kcid")
    private Integer kcid;//所属课程id
    @TableField("type")
    private Integer type;//所属类型:1=作业,2=考试
    @TableField("createtime")
    private Long createtime;//课程创建时间
}
