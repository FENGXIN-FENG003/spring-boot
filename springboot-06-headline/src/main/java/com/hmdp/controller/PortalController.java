package com.hmdp.controller;

import com.hmdp.pojo.PortalVo;
import com.hmdp.service.HeadlineService;
import com.hmdp.service.TypeService;
import com.hmdp.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * @author FENGXIN
 * @date 2024/8/13
 * @project springboot-part
 * @description
 **/
@RestController
@RequestMapping("/portal")
@CrossOrigin
public class PortalController {
    
    @Resource
    private TypeService typeService;
    
    @Resource
    HeadlineService headlineService;
    
    // 展示所有类别
    @GetMapping("/findAllTypes")
    public Result findAllTypes() {
        return typeService.findAllTypes();
    }
    
    // 根据关键字搜索新闻
    @PostMapping("/findNewsPage")
    public Result findNewsPage(@RequestBody PortalVo portalVo) {
        return headlineService.findNewsPage(portalVo);
    }
    
    // 查询头条详细内容
    @PostMapping("showHeadlineDetail")
    public Result showHeadlineDetail(Integer hid) {
        return headlineService.showHeadlineDetail(hid);
    }
}