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
        
        // 封装数据
        Map<String, Object> pageInfo = new HashMap<> ();
        pageInfo.put("pageData",page.getRecords());
        pageInfo.put("pageNum",page.getCurrent());
        pageInfo.put("pageSize",page.getSize());
        pageInfo.put("totalPage",page.getPages());
        pageInfo.put("totalSize",page.getTotal());
        
        // 响应数据
        return Result.ok (pageInfo);
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
}




