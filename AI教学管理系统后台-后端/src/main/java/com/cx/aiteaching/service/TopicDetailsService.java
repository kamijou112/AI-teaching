package com.cx.aiteaching.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cx.aiteaching.entity.TopicDetails;
import com.cx.aiteaching.vo.TopicDetailsVo;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/25 13:59
 */
public interface TopicDetailsService extends IService<TopicDetails> {
    List<TopicDetailsVo> topicDetatail(Integer taskid,Integer tasklogid);
}
