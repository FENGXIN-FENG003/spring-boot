package com.fengxin.springboot.bootweb.controller;

import com.fengxin.springboot.bootweb.pojo.Person;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FENGXIN
 * @date 2024/8/25
 * @project springboot-part
 * @description
 **/
@RestController
public class PersonController {
    
    @GetMapping("/person")
    public Object person(){
        Person person = new Person ();
        person.setId (1);
        person.setName ("枫");
        person.setAge (18);
        return person;
    }
    
    @GetMapping("/e")
    public int exception(){
        return  10 / 0;
    }
    
    // @ExceptionHandler(Exception.class)
    // @ResponseBody
    public String handleException(Exception e){
        return "单类级异常处理" + e.getMessage ();
    }
}
