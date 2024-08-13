package com.fengxin.controller;

import com.fengxin.pojo.PortalVo;
import com.fengxin.service.HeadlineService;
import com.fengxin.service.TypeService;
import com.fengxin.util.Result;
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
}
