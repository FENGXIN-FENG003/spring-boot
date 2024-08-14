package com.fengxin.controller;

import com.fengxin.pojo.Headline;
import com.fengxin.service.HeadlineService;
import com.fengxin.util.JwtHelper;
import com.fengxin.util.Result;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

/**
 * @author FENGXIN
 * @date 2024/8/14
 * @project springboot-part
 * @description
 **/
@RestController
@RequestMapping("headline")
@CrossOrigin
public class HeadlineController {
    
    @Resource
    private HeadlineService headlineService;
    
    @Resource
    private JwtHelper jwtHelper;
    
    // 发布头条
    @PostMapping("publish")
    public Result publish(@RequestBody Headline headline,@RequestHeader String token) {
        // 不用在这里验证token 拦截器自动会校验
        // 直接实现插入数据即可
        // 确定发布者
        headline.setPublisher (jwtHelper.getUserId (token).intValue ());
        return headlineService.publish(headline);
    }
    
    // 响应头条信息 回显头条内容
    @PostMapping("findHeadlineByHid")
    public Result findHeadlineByHid(Integer hid,@RequestHeader String token) {
        // 不用在这里验证token 拦截器自动会校验
        return headlineService.findHeadlineByHid(hid);
    }
    
    // 修改头条内容
    @PostMapping("update")
    public Result update(@RequestBody Headline headline,@RequestHeader String token) {
        // 不用在这里验证token 拦截器自动会校验
        return headlineService.updateNews(headline);
    }
    
    // 删除头条
    @PostMapping("removeByHid")
    public Result removeByHid(@Param ("hid") Integer hid, @RequestHeader String token) {
        return headlineService.removeByHid(hid);
    }
}
