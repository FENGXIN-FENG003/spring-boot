package com.fengxin.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxin.dto.Result;
import com.fengxin.dto.UserDTO;
import com.fengxin.entity.Blog;
import com.fengxin.service.IBlogService;
import com.fengxin.service.IUserService;
import com.fengxin.utils.SystemConstants;
import com.fengxin.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Resource
    private IBlogService blogService;
    @Resource
    private IUserService userService;

    @PostMapping
    public Result saveBlog(@RequestBody Blog blog) {
        return blogService.saveBlog(blog);
    }

    @PutMapping("/like/{id}")
    public Result likeBlog(@PathVariable("id") Long id) {
        return blogService.likeBlog(id);
    }

    @GetMapping("/of/me")
    public Result queryMyBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        // 根据用户查询
        Page<Blog> page = blogService.query()
                .eq("user_id", user.getId()).page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        return Result.ok(records);
    }

    @GetMapping("/hot")
    public Result queryHotBlog(@RequestParam(value = "current", defaultValue = "1") Integer current) {
        return blogService.queryHotBlog(current);
    }
    
    @GetMapping("/{id}")
    public Result queryBlogById(@PathVariable("id") Long id) {
        return blogService.queryBlogById(id);
    }
    
    @GetMapping("/likes/{id}")
    public Result queryBlogLikes(@PathVariable("id") Long id) {
        return blogService.queryBlogLikes(id);
    }
    
    @GetMapping("/of/user")
    public Result queryUserBlog(@RequestParam(value = "current", defaultValue = "1") Integer current,@RequestParam("id") Long id) {
        // 根据用户分页查询发布的blog
        Page<Blog> page = blogService.query ().eq ("user_id" , id).page (new Page<> (current , SystemConstants.MAX_PAGE_SIZE));
        List<Blog> records = page.getRecords ();
        return Result.ok(records);
    }
    
    @GetMapping("/of/follow")
    public Result queryBlogFollow(@RequestParam(value = "lastId") Long max,@RequestParam(value = "offset",defaultValue = "0") Integer offset ) {
        return blogService.queryBlogFollow(max,offset);
    }
}
