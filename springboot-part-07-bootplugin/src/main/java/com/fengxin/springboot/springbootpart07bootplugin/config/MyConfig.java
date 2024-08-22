package com.fengxin.springboot.springbootpart07bootplugin.config;

import com.alibaba.druid.util.DruidDataSourceUtils;
import com.alibaba.druid.util.DruidPasswordCallback;
import com.fengxin.springboot.springbootpart07bootplugin.pojo.Sheep;
import com.fengxin.springboot.springbootpart07bootplugin.pojo.User;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

/**
 * @author FENGXIN
 * @date 2024/8/22
 * @project springboot-part
 * @description 自定义注册组件
 **/
// 该属性绑定常用于第三方绑定 第三方无法添加@Component 在config 中自定义注册@Bean 添加注解@ConfigurationProperties(prefix = "...")
// 然后再使用该注解绑定
    // 该注解自动注册组件 并绑定属性
@EnableConfigurationProperties(Sheep.class)
// @Import可以注册第三方组件 组件名是全类名
@Import (DruidDataSourceUtils.class)
// 和@Configuration没有区别 用于boot项目  前者适用于通用
// MyConfig也会被注入到ioc容器
@SpringBootConfiguration
public class MyConfig {
    /**
     * 默认名是方法名 可以自定义组件名
     * @return user
     */
    @Bean(value = "myUser")
    public User user(){
        User user = new User ();
        user.setId (1);
        user.setName ("枫");
        return user;
    }
    
    /**
     * 导入第三方组件
     * @return druidPasswordCallback
     */
    @Scope(value = "prototype")
    @Bean
    public DruidPasswordCallback druidPasswordCallback(){
        return new DruidPasswordCallback ();
    }

}
