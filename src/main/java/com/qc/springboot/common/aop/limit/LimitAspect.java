package com.qc.springboot.common.aop.limit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.RateLimiter;
import com.qc.springboot.common.enums.LimitType;
import com.qc.springboot.common.enums.ResultCodeEnum;
import com.qc.springboot.common.exception.LimitIpException;
import com.qc.springboot.common.utils.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author qc
 * @create 2020-02-19 9:34
 */
@Aspect
@Configuration
public class LimitAspect {
    //根据IP分不同的令牌桶, 每天自动清理缓存
    private static LoadingCache<String, RateLimiter> caches= CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<String, RateLimiter>() {
                @Override
                public RateLimiter load(String s) throws Exception {
                    //新的ip初始化，每秒只发5个令牌
                    return RateLimiter.create(1);
                }
            });

    /**
     * 把限流注解作为切入点
     */
    @Pointcut("@annotation(com.qc.springboot.common.aop.limit.ServcieLimit)")
    public void serviceAspect(){};
    @Around("serviceAspect()")
    public Object around(ProceedingJoinPoint joinPoint){
         MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ServcieLimit annotation = method.getAnnotation(ServcieLimit.class);
        LimitType limitType = annotation.limitType();
        String key = annotation.key();
        Object object;
        try{
            if(limitType.equals(LimitType.IP)){
                key= IPUtils.getIpAddr();
            }
            RateLimiter rateLimiter = caches.get(key);
            boolean flag = rateLimiter.tryAcquire();
            if(flag){
                object=joinPoint.proceed();
            }else{
                throw new LimitIpException(
                        ResultCodeEnum.LIMIT_IP_ERROR);
            }
        }catch(Throwable e){
            throw new LimitIpException(
                    ResultCodeEnum.LIMIT_IP_ERROR);
        }
        return object;
    }
}
