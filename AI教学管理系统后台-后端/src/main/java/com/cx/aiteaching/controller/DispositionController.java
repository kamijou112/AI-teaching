package com.cx.aiteaching.controller;

import com.cx.aiteaching.common.R;
import com.cx.aiteaching.entity.Disposition;
import com.cx.aiteaching.mapper.DispositionMapper;
import com.cx.aiteaching.service.DispositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 11:47
 */
@RestController
@RequestMapping("/api")
@Tag(name = "公共接口")
public class DispositionController {

    @Resource
    private DispositionMapper dispositionMapper;
    @Resource
    private DispositionService dispositionService;

    @GetMapping("/SystemInfo")
    @Operation(method = "GET",summary = "获取系统信息")
    public R SystemInfo(){
        Disposition disposition = dispositionMapper.selectById(1);
        return R.ok("系统信息获取成功",disposition);
    }

}
