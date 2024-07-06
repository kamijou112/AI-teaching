package com.cx.aiteaching.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cx.aiteaching.entity.TopicDetails;
import com.cx.aiteaching.vo.TopicDetailsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 汪洋
 * @version 1.0
 * @date 2024/6/25 13:59
 */
@Mapper
public interface TopicDetailsMapper extends BaseMapper<TopicDetails> {

    @Select("SELECT a.*,b.tigan,b.type,b.xuanxiang,b.daan FROM `ai_taskdolog` AS a LEFT JOIN  ai_timu as b ON a.timuid = b.id WHERE b.taskid = #{taskid} AND a.tasklogid = #{tasklogid} ")
    List<TopicDetailsVo> topicDetatail(@Param("taskid") Integer taskid,@Param("tasklogid") Integer tasklogid);

}
