package com.qc.springboot.common.aop.redis;

import com.qc.springboot.common.enums.RedisType;

import java.lang.annotation.*;

/**reids自定义注解：用于清除缓存
 * @author qc
 * @create 2020-02-20 11:47
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisEvict {
    /**
     * 键值
     * @return
     */
    String key() default "";

    /**
     * hash的key
     * @return
     */
    String fieldKey() default "" ;

    /**
     * redis的数据类型
     * @return
     */
    RedisType redisType() default RedisType.STRING;
}
