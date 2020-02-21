package com.qc.springboot.common.aop.redis;

import com.qc.springboot.common.enums.ResultCodeEnum;
import com.qc.springboot.common.exception.RedisEvictOrCacheException;
import com.qc.springboot.common.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @author qc
 * @create 2020-02-20 11:51
 */
@Aspect
@Component
@Slf4j
public class ReidsAspect {
    @Autowired
    private RedisUtils redisUtils;
    @Pointcut("@annotation(com.qc.springboot.common.aop.redis.RedisCache)")
    public void redisCachePoint(){}
    @Pointcut("@annotation(com.qc.springboot.common.aop.redis.RedisEvict)")
    public void redisEvictPoint(){}
    @After("redisEvictPoint()")
    public void evict(JoinPoint joinPoint){
        try{
            MethodSignature methodSignature=(MethodSignature)joinPoint.getSignature();
            Method method = methodSignature.getMethod();
            RedisEvict annotation = method.getAnnotation(RedisEvict.class);
            String key = annotation.key();
            String fieldKey = annotation.fieldKey();
            if(!StringUtils.isEmpty(fieldKey)){
                fieldKey=parseKey(fieldKey,method,joinPoint.getArgs());
                redisUtils.hdel(key,fieldKey);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 环绕通知
     * @param joinPoint
     * @return
     */
    @Around("redisCachePoint()")
    public Object WriteReadRedis(ProceedingJoinPoint joinPoint){
        try{
            MethodSignature methodSignature=(MethodSignature)joinPoint.getSignature();
            Method method = methodSignature.getMethod();//获取方法
            Class<?> returnType = method.getReturnType();//获取返回类型
            RedisCache annotation = method.getAnnotation(RedisCache.class);
            String key = annotation.key();
            log.debug("<======method:{} 进入 redisCache 切面 ======>", method.getName());
            String fieldKey=annotation.fieldKey();
            Object obj=null;
            if(!StringUtils.isEmpty(fieldKey)){
                fieldKey=parseKey(annotation.fieldKey(),method,joinPoint.getArgs());
                obj=redisUtils.hget(key,fieldKey,returnType);
            }else {
                obj=redisUtils.get(key,returnType);
            }
            if(obj==null){
                log.debug("<======method:{} 进入 redisCache 切面 ======>", method.getName()+"命中缓存");
                obj=joinPoint.proceed();
                if(!StringUtils.isEmpty(fieldKey)){
                    redisUtils.hset(key,fieldKey,obj);
                }else {
                    if(annotation.expired()>0){
                        redisUtils.set(key, obj,annotation.expired());
                    }else {
                        redisUtils.set(key, obj);
                    }
                }
            }
            return obj;
        }catch(Throwable e){
            log.error("<====== RedisCache 执行异常: {} ======>", e);
            new RedisEvictOrCacheException(ResultCodeEnum.REDIS_RW_ERROR);
        }
        return null;
    }

    /**
     * 获取缓存的key
     * key 定义在注解上，支持SPEL表达式
     * @return
     */
    private String parseKey(String key, Method method, Object[] args)
    {
        // 获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        // 使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        // SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        // 把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++)
        {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}
