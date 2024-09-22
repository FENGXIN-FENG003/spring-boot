package com.fengxin.pojo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author FENGXIN
 * @date 2024/8/8
 * @project springboot-part
 * @description
 **/
@Component
public class DataSource {
    @Value ("${fx.root.username}")
    private String username;
    
    @Value ("${fx.root.password}")
    private String password;
    
    public String getUsername () {
        return username;
    }
    
    public void setUsername (String username) {
        this.username = username;
    }
    
    public String getPassword () {
        return password;
    }
    
    public void setPassword (String password) {
        this.password = password;
    }
    
    @Override
    public String toString () {
        return "DataSource{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
