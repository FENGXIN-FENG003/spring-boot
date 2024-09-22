package com.fengxin.controller;


import com.fengxin.dto.Result;
import com.fengxin.service.IFollowService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/follow")
public class FollowController {
    @Resource
    private IFollowService followService;
    
    /**
     * 关注 取关
     */
    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable Long id, @PathVariable Boolean isFollow) {
        return followService.follow(id,isFollow);
    }
    
    /**
     * 是否关注
     */
    @GetMapping("/or/not/{id}")
    public Result isFollow(@PathVariable Long id) {
        return followService.isFollow(id);
    }
    
    /**
     * 共同关注
     */
    @GetMapping("/common/{id}")
    public Result common(@PathVariable Long id) {
        return followService.common(id);
    }
}
