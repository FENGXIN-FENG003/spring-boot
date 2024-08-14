package com.fengxin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxin.pojo.Headline;
import com.fengxin.pojo.PortalVo;
import com.fengxin.service.HeadlineService;
import com.fengxin.mapper.HeadlineMapper;
import com.fengxin.util.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* @author FENGXIN
* @description 针对表【news_headline】的数据库操作Service实现
* @createDate 2024-08-12 13:33:39
*/
@Service
public class HeadlineServiceImpl extends ServiceImpl<HeadlineMapper, Headline>
    implements HeadlineService{
    
    @Resource
    private HeadlineMapper headlineMapper;
    /**
     * 返回前端请求的分页数据
     */
    @Override
    public Result findNewsPage (PortalVo portalVo) {
        
        // 自定义查询
        // 分页指定 返回dataMap
        IPage<Map> page = new Page<> (portalVo.getPageNum () , portalVo.getPageSize ());
        
        // 执行自定义查询
        headlineMapper.selectMyPage (page,portalVo);
        System.out.println ("page.getRecords () = " + page.getRecords ());
        // 封装数据
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> pageInfo = new HashMap<>();
        pageInfo.put("pageData",page.getRecords());
        pageInfo.put("pageNum",page.getCurrent());
        pageInfo.put("pageSize",page.getSize());
        pageInfo.put("totalPage",page.getPages());
        pageInfo.put("totalSize",page.getTotal());
        map.put("pageInfo",pageInfo);
        // 响应数据
        return Result.ok (map);
    }
    
    /**
     * 返回头条详细内容
     * @param hid 前端参数
     * @return 详细内容
     */
    @Override
    public Result showHeadlineDetail (Integer hid) {
        
        // 查询内容
        Map headlineDetail = headlineMapper.selectHeadlineDetail (hid);
        
        // 更新浏览量
        Headline headline = new Headline ();
        headline.setHid (hid);
        headline.setPageViews ((Integer) headlineDetail.get ("pageViews") + 1);
        headlineMapper.updateById (headline);
        
        // 返回数据
        return Result.ok (headlineDetail);
    }
    
    /**
     * 插入头条数据
     */
    @Override
    public Result publish (Headline headline) {
        headline.setCreateTime (new Date ());
        headline.setUpdateTime (new Date ());
        headline.setPageViews (0);
        headlineMapper.insert (headline);
        System.out.println ("headline = " + headline);
        return Result.ok (headline);
    }
    
    /**
     * 头条信息回显
     */
    @Override
    public Result findHeadlineByHid (Integer hid) {
        Headline headline = headlineMapper.selectById (hid);
        // Map newsInformation = headlineMapper.selectNewsInformation (hid);
        Map<String,Object> map = new HashMap<>();
        map.put("headline",headline);
        return Result.ok (map);
        
    }
    
    /**
     * 更新头条内容
     */
    @Override
    public Result updateNews (Headline headline) {
        // 读取版本
        Integer version = headlineMapper.selectById (headline.getHid ()).getVersion ();
        headline.setVersion (version + 1);
        headline.setUpdateTime (new Date ());
        headlineMapper.updateById (headline);
        return Result.ok (null);
    }
}




