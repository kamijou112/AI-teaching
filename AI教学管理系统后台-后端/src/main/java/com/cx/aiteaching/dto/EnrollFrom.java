package com.cx.aiteaching.dto;

import lombok.Data;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 16:54
 */
@Data
//注册表单
public class EnrollFrom {

    private String name;

    private String username;

    private String password;

    private Integer sex;
}
