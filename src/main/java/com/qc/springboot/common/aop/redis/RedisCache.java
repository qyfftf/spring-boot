package com.qc.springboot.common.aop.redis;

import com.qc.springboot.common.enums.RedisType;

import java.lang.annotation.*;

/**自定义redis缓存注解
 * @author qc
 * @create 2020-02-20 11:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisCache {
    /**
     * 键名
     * @return
     */
    String key() default "";

    /**
     * hash数据类型的key
     * @return
     */
    String fieldKey() default "";

    /**
     * redis的数据类型
     * @return
     */
    RedisType redisType() default RedisType.STRING;
    /**
     * 过期时间
     * @return
     */
    long expired() default 3600;
}
