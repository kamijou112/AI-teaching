package com.cx.aischeduledtasks.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cx.aischeduledtasks.entity.TopicDetails;
import com.cx.aischeduledtasks.mapper.TopicDetailsMapper;
import com.cx.aischeduledtasks.service.TopicDetailsService;
import com.cx.aischeduledtasks.vo.TopicDetailsVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/25 13:59
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TopicDetailsServiceImpl extends ServiceImpl<TopicDetailsMapper, TopicDetails> implements TopicDetailsService {
    @Resource
    private TopicDetailsMapper topicDetailsMapper;

    @Override
    public List<TopicDetailsVo> topicDetatail(Integer taskid) {
        return topicDetailsMapper.topicDetatail(taskid);
    }
}
