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
 * @date 2024/6/24 15:09
 */
@Data
@TableName("ai_timu")
public class Topic implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//题目id
    @TableField("taskid")
    private Integer taskid;//题目所属任务id
    @TableField("type")
    private Integer type;//题目类型:1=填空,2=单选,3=判断
    @TableField("tigan")
    private String tigan;//题干
    @TableField("xuanxiang")
    private String xuanxiang;//选项,用|隔开,只有当type为2时有
    @TableField("daan")
    private String daan;//  答案,如果type是2或3则用1，2，3代替答案
    @TableField("createtime")
    private Long createtime;//创建时间
}
