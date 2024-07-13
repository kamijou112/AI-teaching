package com.cx.aischeduledtasks.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cx.aischeduledtasks.entity.TopicDetails;
import com.cx.aischeduledtasks.vo.TopicDetailsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/25 13:59
 */
@Mapper
public interface TopicDetailsMapper extends BaseMapper<TopicDetails> {

    @Select("SELECT a.*,b.tigan,b.type,b.xuanxiang,b.daan FROM `ai_taskdolog` AS a LEFT JOIN  ai_timu as b ON a.timuid = b.id WHERE b.taskid = #{taskid}")
    List<TopicDetailsVo> topicDetatail(Integer taskid);

}
