package com.hmdp.controller;


import com.hmdp.dto.Result;
import com.hmdp.service.IFollowService;
import com.hmdp.service.impl.FollowServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.nio.channels.Pipe;

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
}
