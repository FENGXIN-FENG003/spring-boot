package com.fengxin.advance;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/
@Component
@Aspect
public class EmpAdvance {
    @Before ("com.fengxin.point.CutPoint.cutService()")
    public void pre(JoinPoint joinPoint) {
        String className = joinPoint.getTarget ().getClass ().getSimpleName ();
        String methodName = joinPoint.getSignature ().getName ();
        System.out.println ("className = " + className + ", methodName = " + methodName);
    }
}
