package com.fengxin.springboot.bootplugin.pojo;

/**
 * @author FENGXIN
 * @date 2024/8/22
 * @project springboot-part
 * @description
 **/
public class User {
    private Integer id;
    private String name;
    
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
}
