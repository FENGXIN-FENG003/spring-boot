package com.fengxin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fengxin.pojo.Headline;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengxin.pojo.PortalVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
* @author FENGXIN
* @description 针对表【news_headline】的数据库操作Mapper
* @createDate 2024-08-12 13:33:39
* @Entity com.fengxin.pojo.Headline
*/
@Mapper
public interface HeadlineMapper extends BaseMapper<Headline> {
    /**
     * 自定义查询
     * @param page 分页插件 数据存储在这里 @Param ("portalVo")指定参数名方便数据库查询使用
     * @param portalVo 自定义sql中需要用到的参数
     */

    IPage<Map> selectMyPage (IPage page ,@Param ("portalVo") PortalVo portalVo);
    

    Map selectHeadlineDetail(@Param ("hid") Integer hid);
    

    Map selectNewsInformation(@Param ("hid") Integer hid);
}




