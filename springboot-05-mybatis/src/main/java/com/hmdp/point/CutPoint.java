package com.hmdp.point;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/
@Component
public class CutPoint {
    
    @Pointcut("execution(* com..service.*.*(..))")
    public void cutService() {}
}
