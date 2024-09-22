package com.fengxin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengxin.dto.Result;
import com.fengxin.dto.ScrollResult;
import com.fengxin.dto.UserDTO;
import com.fengxin.entity.Blog;
import com.fengxin.entity.Follow;
import com.fengxin.entity.User;
import com.fengxin.mapper.BlogMapper;
import com.fengxin.service.IBlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxin.service.IFollowService;
import com.fengxin.service.IUserService;
import com.fengxin.utils.RedisConstants;
import com.fengxin.utils.SystemConstants;
import com.fengxin.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    @Resource
    private IFollowService followService;
    
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
                // 确保查询顺序正确 （in）
                .last ("order by field(id," + idStr + ")").list ()
                .stream ().map (user -> BeanUtil.copyProperties (user, UserDTO.class)).toList ();
        // 返回
        return Result.ok (userDTO);
    }
    
    @Override
    public Result saveBlog (Blog blog) {
        // 获取登录用户
        UserDTO user = UserHolder.getUser();
        blog.setUserId(user.getId());
        // 保存探店博文
        boolean save = save (blog);
        // 保存blog成功后 查询所有粉丝id 存入redis 一个粉丝拥有一个收件箱 存放该用户的id和时间戳 推送blog
        if (!save) {
            return Result.fail ("新增笔记失败");
        }
        // 查询所有粉丝id
        List<Follow> fans = followService.query ().eq ("follow_user_id" , user.getId ()).list ();
        // 推送
        if (fans == null || fans.isEmpty ()) {
            return Result.ok (blog);
        }
        // 粉丝id存入redis
        fans.forEach (follow -> {
            Long fansId = follow.getUserId ();
            stringRedisTemplate.opsForZSet ().add (RedisConstants.FEED_KEY + fansId,blog.getUserId ().toString (),System.currentTimeMillis ());
        });
        // 返回id
        return Result.ok(blog.getId());
    }
    
    @Override
    public Result queryBlogFollow (Long max , Integer offset) {
        // 1.获取用户id
        Long id = UserHolder.getUser ().getId ();
        // 2.查询关注的用户id和时间戳
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate
                .opsForZSet ()
                .rangeByScoreWithScores (RedisConstants.FEED_KEY + id , 0 , max , offset , 2);
        // 3.查询blog minTime offset
        if (typedTuples == null || typedTuples.isEmpty ()) {
            return Result.ok (Collections.emptyList ());
        }
        // 3.1存储userId集合
        List<Long> idList = new ArrayList<> (typedTuples.size ());
        long minTime = 0;
        int offsetCount = 1;
        // 根据关注的用户id和时间戳
        for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
            // 3.2查询userId
            idList.add(Long.valueOf (typedTuple.getValue ()));
            // 3.3获取时间戳
            long score = typedTuple.getScore ().longValue ();
            // 3.4设置offset
            if (minTime == score){
                offsetCount++;
            }else {
                minTime = score;
                offsetCount = 1;
            }
        }
        // 3.5根据userId查询关注的用户blog
        String idStr = StrUtil.join ("," , idList);
        // 保证数据查询结果顺序一致
        List<Blog> blogs = query ().in ("user_id" , idList).last ("order by field(id," + idStr + ")").list ();
        // 3.6完整blog数据
        blogs.forEach (blog -> {
            extracted (blog);
            isLiked (blog);
        });
        // 4.设置返回结果
        ScrollResult scrollResult = new ScrollResult ();
        scrollResult.setList (blogs);
        scrollResult.setMinTime (minTime);
        scrollResult.setOffset (offsetCount);
        
        return Result.ok (scrollResult);
    }
    
}
