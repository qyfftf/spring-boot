package com.qc.springboot.service.impl;

import com.qc.springboot.common.aop.redis.RedisCache;
import com.qc.springboot.common.aop.redis.RedisEvict;
import com.qc.springboot.common.enums.RedisType;
import com.qc.springboot.entities.Test;
import com.qc.springboot.mapper.TestMapper;
import com.qc.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qc
 * @create 2020-02-20 17:54
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    TestMapper testMapper;
    @Override
    @RedisCache(key = "dept",fieldKey = "#id",redisType = RedisType.HASH)
    public Test getById(Integer id) {
        return testMapper.getByid(id);
    }

    @Override
    @RedisEvict(key = "dept",fieldKey = "#id",redisType = RedisType.HASH)
    public void updateById(int id,String name) {
        testMapper.updateById(id,name);
    }
}
