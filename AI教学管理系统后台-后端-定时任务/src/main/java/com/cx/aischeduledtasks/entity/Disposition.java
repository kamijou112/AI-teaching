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
 * @date 2024/6/24 11:44
 */
@Data
@TableName("ai_config")
public class Disposition implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;//主键id
    @TableField("webtitle")
    private String webtitle;//网站标题
    @TableField("logo")
    private String logo;//网站logo
}
