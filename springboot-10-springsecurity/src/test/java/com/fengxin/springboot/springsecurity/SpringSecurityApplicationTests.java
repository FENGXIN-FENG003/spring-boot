package com.fengxin.springboot.springsecurity;

import com.fengxin.springboot.springsecurity.mapper.UserMapper;
import com.fengxin.springboot.springsecurity.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

@SpringBootTest
class SpringSecurityApplicationTests {
    @Autowired
    private UserMapper userMapper;
    @Test
    void contextLoads ()
    {
        List<User> users = userMapper.selectList (null);
        System.out.println (users);
    }
    
    @Test
    void testCode(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder ();
        String encode = passwordEncoder.encode ("000000");
        System.out.println (encode);
        System.out.println (passwordEncoder.matches ("000000" , encode));
    }
    
    
}
