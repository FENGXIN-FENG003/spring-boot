package com.fengxin.springboot.bootweb.pojo;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @author FENGXIN
 * @date 2024/8/25
 * @project springboot-part
 * @description
 **/
@JacksonXmlRootElement
@Data
public class Person {
    private Integer id;
    private String name;
    private Integer age;
}
