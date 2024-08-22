package com.fengxin.springboot.springbootpart07bootplugin.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author FENGXIN
 * @date 2024/8/22
 * @project springboot-part
 * @description
 **/
@ConfigurationProperties(prefix = "sheep")
public class Sheep {
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
