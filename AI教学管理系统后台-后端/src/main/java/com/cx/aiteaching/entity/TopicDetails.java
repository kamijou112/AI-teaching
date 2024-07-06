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
 * @date 2024/6/25 13:41
 */
@Data
@TableName("ai_taskdolog")
public class TopicDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//做过题目id
    @TableField("tasklogid")
    private Integer tasklogid;//所属任务进度id
    @TableField("timuid")
    private Integer timuid;//所属题目id
    @TableField("zuoda")
    private String  zuoda;//学生作答内容
    @TableField("isdui")
    private Integer  isdui;//学生作答内容
    @TableField("aijiexi")
    private String aijiexi;//ai解析
    @TableField("createtime")
    private Long createtime;//创建时间

}
