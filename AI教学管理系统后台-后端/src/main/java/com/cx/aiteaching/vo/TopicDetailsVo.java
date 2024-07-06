package com.cx.aiteaching.vo;

import lombok.Data;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/25 14:25
 */
@Data
public class TopicDetailsVo {

    private Integer id;//做过题目id

    private Integer tasklogid;//所属任务进度id

    private Integer timuid;//所属题目id

    private String  zuoda;//学生作答内容

    private Integer  isdui;//学生作答内容

    private String aijiexi;//ai解析

    private Long createtime;//创建时间

    private String tigan;

    private Integer type;

    private String xuanxiang;

    private String daan;
}
