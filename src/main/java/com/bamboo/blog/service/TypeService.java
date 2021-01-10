package com.bamboo.blog.service;

import com.bamboo.blog.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface TypeService {
    //新增type并保存，传进来要保存的type
    Type saveType(Type type);
    //查询
    Type getType(Long id);

    Type getTypeByName(String name);
    // 分页查询
    Page<Type> listType(Pageable pageable);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);

    //修改
    Type updateType(Long id, Type type);

    void deleteType(Long id);
}
