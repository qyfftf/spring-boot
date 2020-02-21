package com.qc.springboot.common.exception;

import com.qc.springboot.common.enums.ResultCodeEnum;

/**
 * @author qc
 * @create 2020-02-20 16:36
 */
public class RedisEvictOrCacheException extends RuntimeException{
    private Integer code;
    public RedisEvictOrCacheException(String message,Integer code){
        super(message);
        this.code=code;
    }

    public RedisEvictOrCacheException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code=resultCodeEnum.getCode();
    }
}
