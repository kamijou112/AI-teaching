package com.cx.aiteaching.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aiteaching.entity.Topic;
import com.cx.aiteaching.mapper.TopicMapper;
import com.cx.aiteaching.service.TopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/24 15:28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {
}
