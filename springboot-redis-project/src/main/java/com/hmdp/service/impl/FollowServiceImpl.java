package com.hmdp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.dto.UserDTO;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.service.IUserService;
import com.hmdp.utils.RedisConstants;
import com.hmdp.utils.UserHolder;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private IUserService userService;
    @Override
    public Result follow (Long id , Boolean isFollow) {
        // 获取当前登录用户id
        Long userId = UserHolder.getUser ().getId ();
        
        if (isFollow) {
            // true 关注
            Follow follow = new Follow ();
            follow.setUserId (userId);
            follow.setFollowUserId (id);
            boolean save = save (follow);
            if (save){
                // 存入redis方便查询共同用户
                stringRedisTemplate.opsForSet ().add (RedisConstants.BLOG_FOLLOW_KEY + userId,id.toString ());
            }
        }else {
            // false 取关
            boolean remove = remove (new LambdaQueryWrapper<Follow> ().eq (Follow::getUserId , userId).eq (Follow::getFollowUserId , id));
            if (remove){
                stringRedisTemplate.delete(RedisConstants.BLOG_FOLLOW_KEY + userId);
            }
        }
        return Result.ok ();
    }
    
    @Override
    public Result isFollow (Long id) {
        Long userId = UserHolder.getUser ().getId ();
        Long count = query ()
                .getBaseMapper ()
                .selectCount (new LambdaQueryWrapper<Follow> ()
                        .eq (Follow::getUserId , userId).eq (Follow::getFollowUserId , id));
        return Result.ok ( count > 0);
    }
    
    @Override
    public Result common (Long id) {
        // 获取当前用户key
        Long userId = UserHolder.getUser ().getId ();
        String key = RedisConstants.BLOG_FOLLOW_KEY + userId;
        // 获取访问的用户key
        String key2 = RedisConstants.BLOG_FOLLOW_KEY + id;
        // 查询共同关注
        Set<String> intersect = stringRedisTemplate.opsForSet ().intersect (key , key2);
        if (intersect == null || intersect.isEmpty ()){
            return Result.ok (Collections.emptyList ());
        }
        // 提取用户id
        List<Long> userIdList = intersect.stream ().map (Long::valueOf).toList ();
        // 查询用户
        List<UserDTO> list = userService.listByIds (userIdList)
                .stream ()
                .map (user -> BeanUtil.copyProperties (user , UserDTO.class))
                .toList ();
        return Result.ok (list);
    }
}
