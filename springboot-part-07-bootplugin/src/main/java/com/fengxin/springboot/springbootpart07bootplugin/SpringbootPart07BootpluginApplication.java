package com.fengxin.springboot.springbootpart07bootplugin;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.fengxin.springboot.springbootpart07bootplugin.pojo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author FENGXIN
 */
@SpringBootApplication
// scanBasePackages属性可以指定扫描包路径 但是约定大于配置 一般不建议这样做
// @SpringBootConfiguration + @ComponentScan + @EnableAutoConfiguration = @SpringBootApplication
public class SpringbootPart07BootpluginApplication {
    
    public static void main (String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run (SpringbootPart07BootpluginApplication.class , args);
        for (String beanDefinitionName : ioc.getBeanDefinitionNames ()) {
            System.out.println ("beanDefinitionName = " + beanDefinitionName);
        }
        // 检查单例多例
        User user1 = (User) ioc.getBean ("myUser");
        User user2 = (User) ioc.getBean ("myUser");
        System.out.println (" singleton " + (user1 == user2));
        
        DruidPasswordCallback druidPasswordCallback1 = ioc.getBean (DruidPasswordCallback.class);
        DruidPasswordCallback druidPasswordCallback2 = ioc.getBean (DruidPasswordCallback.class);
        System.out.println (" prototype " + (druidPasswordCallback1 == druidPasswordCallback2));
        
        
    }
    
}
