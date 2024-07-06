package com.cx.aischeduledtasks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aischeduledtasks.entity.Topic;
import com.cx.aischeduledtasks.mapper.TopicMapper;
import com.cx.aischeduledtasks.service.TopicService;
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
