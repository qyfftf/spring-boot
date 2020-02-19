package com.qc.springboot.common.exception;

import com.qc.springboot.common.enums.ResultCodeEnum;

/**
 * @author qc
 * @create 2020-02-19 9:57
 */
public class LimitIpException extends RuntimeException{
    //错误码
    private Integer code;
    public LimitIpException(Integer code,String message){
        super(message);
        this.code=code;
    }

    public LimitIpException(ResultCodeEnum resultCodeEnum){
        super(resultCodeEnum.getMessage());
        this.code=resultCodeEnum.getCode();
    }
}
