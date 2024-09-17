package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
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

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (userId == null){
            return;
        }
        // 判断是否点过赞
        Double score = stringRedisTemplate.opsForZSet ().score (key , userId.toString ());
        if (score != null) {
            blog.setIsLike (true);
        }
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
    
    /**
     * 点赞功能
     */
    @Override
    public Result likeBlog (Long id) {
        String key = RedisConstants.BLOG_LIKED_KEY + id;
        Long userId = UserHolder.getUser ().getId ();
        // 判断是否点过赞
        Double score = stringRedisTemplate.opsForZSet ().score (key , userId.toString ());
        // 未点赞 点赞
        if (score == null) {
            // 数据库 点赞数 +1
            boolean success = update ().setSql ("liked = liked + 1").eq ("id" , id).update ();
            if (success) {
                // redis 添加用户id
                stringRedisTemplate.opsForZSet ().add (key,userId.toString (),System.currentTimeMillis ());
            }
        }else {
            // 已经点赞 取消点赞
            // 数据库 点赞数 -1
            boolean success = update ().setSql ("liked = liked - 1").eq ("id" , id).update ();
            // redis 移除用户id
            if (success) {
                // redis 添加用户id
                stringRedisTemplate.opsForZSet ().remove (key,userId.toString ());
            }
        }
        
        return Result.ok ();
    }
    
    /**
     * 用户点赞排行 根据时间
     */
    @Override
    public Result queryBlogLikes (Long id) {
        // 查询redis用户点赞
        Set<String> top5 = stringRedisTemplate.opsForZSet ().range (RedisConstants.BLOG_LIKED_KEY + id , 0 , 4);
        if (top5 == null || top5.isEmpty ()) {
            return Result.ok (Collections.emptyList ());
        }
        // 解析userId
        List<Long> idList = top5.stream ().map (Long::valueOf).toList ();
        // 根据userId查询user
        String idStr = StrUtil.join ("," , idList);
        List<UserDTO> userDTO = userService.query ()
                .in ("id" , idList)
                .last ("order by field(id," + idStr + ")").list ()
                .stream ().map (user -> BeanUtil.copyProperties (user, UserDTO.class)).toList ();
        // 返回
        return Result.ok (userDTO);
    }
    
}
