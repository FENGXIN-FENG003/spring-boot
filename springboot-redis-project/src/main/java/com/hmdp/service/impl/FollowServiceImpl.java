package com.hmdp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;
import com.hmdp.mapper.FollowMapper;
import com.hmdp.service.IFollowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.utils.UserHolder;
import org.springframework.stereotype.Service;

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
    
    @Override
    public Result follow (Long id , Boolean isFollow) {
        // 获取当前登录用户id
        Long userId = UserHolder.getUser ().getId ();
        
        if (isFollow) {
            // true 关注
            Follow follow = new Follow ();
            follow.setUserId (userId);
            follow.setFollowUserId (id);
            save(follow);
        }else {
            // false 取关
            remove (new LambdaQueryWrapper<Follow> ().eq(Follow::getFollowUserId,userId).eq(Follow::getFollowUserId,id));
        }
        return Result.ok ();
    }
    
    @Override
    public Result isFollow (Long id) {
        Long userId = UserHolder.getUser ().getId ();
        Long count = query ()
                .getBaseMapper ()
                .selectCount (new LambdaQueryWrapper<Follow> ()
                        .eq (Follow::getFollowUserId , userId).eq (Follow::getFollowUserId , id));
        return Result.ok (count != null && count > 0);
    }
}
