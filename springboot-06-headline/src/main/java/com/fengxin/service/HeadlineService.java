package com.fengxin.service;

import com.fengxin.pojo.Headline;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxin.pojo.PortalVo;
import com.fengxin.util.Result;

/**
* @author FENGXIN
* @description 针对表【news_headline】的数据库操作Service
* @createDate 2024-08-12 13:33:39
*/
public interface HeadlineService extends IService<Headline> {
    
    Result findNewsPage (PortalVo portalVo);
    
    Result showHeadlineDetail (Integer hid);
    
    Result publish (Headline headline);
    
    Result findHeadlineByHid (Integer hid);
    
    Result updateNews (Headline headline);
    
    Result removeByHid (Integer hid);
    
}

