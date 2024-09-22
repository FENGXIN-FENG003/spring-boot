package com.fengxin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengxin.pojo.Type;
import com.fengxin.service.TypeService;
import com.fengxin.mapper.TypeMapper;
import com.fengxin.util.Result;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author FENGXIN
* @description 针对表【news_type】的数据库操作Service实现
* @createDate 2024-08-12 13:33:39
*/
@Service
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type>
    implements TypeService{
    
    @Resource
    private TypeMapper typeMapper;
    
    
    /**
     * 返回所有头条类别
     * @return Result
     */
    @Override
    public Result findAllTypes () {
        List<Type> list = typeMapper.selectList(null);
        System.out.println ("All types : " + list);
        return Result.ok(list);
    }
}




