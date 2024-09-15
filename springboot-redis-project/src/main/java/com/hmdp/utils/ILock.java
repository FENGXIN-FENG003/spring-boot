package com.hmdp.utils;

/**
 * @author FENGXIN
 * @date 2024/9/15
 * @project springboot-part
 * @description 锁接口
 **/
public interface ILock {
    boolean tryLock(long timeout);
    void unLock();
}
