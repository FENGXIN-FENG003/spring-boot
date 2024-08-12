package com.fengxin.mapper;

import com.fengxin.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author FENGXIN
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-08-12 13:33:39
* @Entity com.fengxin.pojo.Headline
*/
@Mapper
public interface HeadlineMapper extends BaseMapper<Headline> {

}




