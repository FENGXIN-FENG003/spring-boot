package com.fengxin.springboot.springbootpart07bootplugin.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author FENGXIN
 * @date 2024/8/22
 * @project springboot-part
 * @description
 * 属性绑定
 * 1.注册组件<br>
 *  1.1 类加@Component<br>
 *  1.2 config方法@bean<br>
 * 2.注解绑定配置文件properties前缀@ConfigurationProperties(prefix = "pig")<br>
 *  2.1 类注解
 *  2.2 config方法@bean添加注解
 **/
@Component
@ConfigurationProperties(prefix = "pig")
public class Pig {
    private Integer id;
    private String name;
    private Integer age;
    
    @Override
    public String toString () {
        return "Pig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
    
    public Integer getId () {
        return id;
    }
    
    public void setId (Integer id) {
        this.id = id;
    }
    
    public String getName () {
        return name;
    }
    
    public void setName (String name) {
        this.name = name;
    }
    
    public Integer getAge () {
        return age;
    }
    
    public void setAge (Integer age) {
        this.age = age;
    }
}
