package com.fengxin.service;

import com.fengxin.pojo.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengxin.util.Result;

/**
* @author FENGXIN
* @description 针对表【news_type】的数据库操作Service
* @createDate 2024-08-12 13:33:39
*/
public interface TypeService extends IService<Type> {
    
    Result findAllTypes ();
    
}
