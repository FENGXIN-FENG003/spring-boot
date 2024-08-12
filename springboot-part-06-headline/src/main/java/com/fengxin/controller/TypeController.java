package com.fengxin.controller;

import com.fengxin.pojo.Type;
import com.fengxin.service.TypeService;
import com.fengxin.util.Result;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author FENGXIN
 * @date 2024/8/12
 * @project springboot-part
 * @description
 **/
@RestController
@RequestMapping("/portal")
@CrossOrigin
public class TypeController {
    @Resource
    private TypeService typeService;
    
    // 展示所有类别
    @GetMapping("/findAllTypes")
    public Result findAllTypes() {
        return typeService.findAllTypes();
    }
}
