package com.qc.springboot.common.handler;

import com.qc.springboot.common.dto.R;
import com.qc.springboot.common.enums.ResultCodeEnum;
import com.qc.springboot.common.exception.LimitIpException;
import com.qc.springboot.common.exception.RedisEvictOrCacheException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**异常处理器
 * @author qc
 * @create 2020-02-19 10:15
 */
@RestControllerAdvice
@Slf4j
public class ExecptionHandler {
    /**
     * 自定义限流异常
     */
    @ExceptionHandler(LimitIpException.class)
    public R limitIpException(){
        return R.setResult(ResultCodeEnum.LIMIT_IP_ERROR);
    }
    @ExceptionHandler(RedisEvictOrCacheException.class)
    public R redisCacheOrEvictException(){
        return R.setResult(ResultCodeEnum.REDIS_RW_ERROR);
    }
}
