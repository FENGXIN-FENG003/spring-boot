package com.fengxin.springboot.springbootpart07bootplugin.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceMBean;
import com.fengxin.springboot.springbootpart07bootplugin.pojo.Cat;
import com.fengxin.springboot.springbootpart07bootplugin.pojo.Dog;
import com.fengxin.springboot.springbootpart07bootplugin.pojo.User;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;

/**
 * @author FENGXIN
 * @date 2024/8/22
 * @project springboot-part
 * @description 当条件注解应用于类级时 满足条件则类中所有方法都生效
 **/
@SpringBootConfiguration
public class ConditionConfig {
    @ConditionalOnClass(User.class)
    @Bean
    public Cat onUserCat(){
        return new Cat ();
    }
    
    @ConditionalOnMissingClass("com.fengxin.springboot.springbootpart07bootplugin.pojo.User")
    @Bean
    public Dog missingUserDog(){
        return new Dog ();
    }
    
    @ConditionalOnBean(Cat.class)
    @Bean
    public DruidDataSource onCat(){
        return new DruidDataSource ();
    }
    
    @ConditionalOnMissingBean(Cat.class)
    @Bean
    public DruidDataSourceMBean missingCat(){
        return new DruidDataSource ();
    }
}
