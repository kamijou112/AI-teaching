package com.cx.aiteaching.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aiteaching.entity.Disposition;
import com.cx.aiteaching.mapper.DispositionMapper;
import com.cx.aiteaching.service.DispositionService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 11:48
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DispositionServiceImpl extends ServiceImpl<DispositionMapper, Disposition> implements DispositionService {
    @Resource
    private DispositionMapper dispositionMapper;

}
