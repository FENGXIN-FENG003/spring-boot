package com.hmdp.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.entity.Blog;
import com.hmdp.entity.User;
import com.hmdp.mapper.BlogMapper;
import com.hmdp.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.SystemConstants;
import com.hmdp.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 枫
 * @since 2021-12-22
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
    @Resource
    private IUserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryHotBlog (Integer current) {
        // 根据用户查询
        Page<Blog> page = query()
                .orderByDesc("liked")
                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
        // 获取当前页数据
        List<Blog> records = page.getRecords();
        // 查询用户
        records.forEach(blog -> {
            extracted (blog);
            isLiked (blog);
        });
        return Result.ok(records);
    }
    
    /**
     * 存储userid到blog
     */
    private void extracted (Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }
    
    /**
     * 设置用户点过赞blog
     */
    public void isLiked(Blog blog) {
        String key = RedisConstants.BLOG_LIKED_KEY + blog.getId();
        Long userId = UserHolder.getUser ().getId ();
        // 判断是否点过赞
        Boolean member = stringRedisTemplate.opsForSet ().isMember (key , userId.toString());
        blog.setIsLike (Boolean.TRUE.equals(member));
    }
    
    /**
     * 浏览用户blog
     */
    @Override
    public Result queryBlogById (Long id) {
        // 查询blog
        Blog blog = getById (id);
        if (blog == null) {
            return Result.fail ("笔记不存在！");
        }
        // 查询用户
        extracted (blog);
        // 查询是否已点过赞
        isLiked (blog);
        return Result.ok (blog);
    }
    
    @Override
    public Result likeBlog (Long id) {
        String key = RedisConstants.BLOG_LIKED_KEY + id;
        Long userId = UserHolder.getUser ().getId ();
        // 判断是否点过赞
        Boolean member = stringRedisTemplate.opsForSet ().isMember (key , userId.toString());
        // 未点赞 点赞
        if (Boolean.FALSE.equals (member)) {
            // 数据库 点赞数 +1
            boolean success = update ().setSql ("liked = liked + 1").eq ("id" , id).update ();
            if (success) {
                // redis 添加用户id
                stringRedisTemplate.opsForSet ().add (key,userId.toString ());
            }
        }else {
            // 已经点赞 取消点赞
            // 数据库 点赞数 -1
            boolean success = update ().setSql ("liked = liked - 1").eq ("id" , id).update ();
            // redis 移除用户id
            if (success) {
                // redis 添加用户id
                stringRedisTemplate.opsForSet ().remove (key,userId.toString ());
            }
        }
        
        return Result.ok ();
    }
    
}
