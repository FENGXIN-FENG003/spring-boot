package com.fengxin;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author FENGXIN
 * @date 2024/8/12
 * @project springboot-part
 * @description
 **/
@SpringBootApplication
public class Application {
    public static void main (String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor ());
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor ());
        interceptor.addInnerInterceptor (new BlockAttackInnerInterceptor ());
        return interceptor;
    }
}
