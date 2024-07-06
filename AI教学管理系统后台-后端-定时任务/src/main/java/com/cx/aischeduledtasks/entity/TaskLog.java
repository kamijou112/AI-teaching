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
 * @date 2024/6/24 18:16
 */
@Data
@TableName("ai_tasklog")
public class TaskLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//任务记录id

    @TableField("studentid")
    private Integer studentid;//学生id
    @TableField("taskid")
    private Integer taskid;//任务id
    @TableField("status")
    private Integer status;//状态:1=待提交,2=已提交AI评卷中,3=已完成
    @TableField("aipingjia")
    private String  aipingjia;//ai评价
    @TableField("createtime")
    private Long    createtime;//创建时间
}
