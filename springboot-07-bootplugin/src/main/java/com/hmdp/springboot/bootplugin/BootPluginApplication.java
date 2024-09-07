package com.hmdp.springboot.bootplugin;

import com.alibaba.druid.util.DruidPasswordCallback;
import com.hmdp.springboot.bootplugin.pojo.Pig;
import com.hmdp.springboot.bootplugin.pojo.Sheep;
import com.hmdp.springboot.bootplugin.pojo.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author FENGXIN
 */
// scanBasePackages属性可以指定扫描包路径 但是约定大于配置 一般不建议这样做
// @SpringBootConfiguration + @ComponentScan + @EnableAutoConfiguration = @SpringBootApplication
@SpringBootApplication
public class BootPluginApplication {
    
    public static void main (String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run (BootPluginApplication.class , args);
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
        
        Pig pigBean = ioc.getBean (Pig.class);
        System.out.println ("pigBean = " + pigBean);
        
        Sheep sheepBean = ioc.getBean (Sheep.class);
        System.out.println ("sheepBean = " + sheepBean);
    }
    
}
