package com.qc.springboot.common.aop.redis;

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
     * 过期时间
     * @return
     */
    long expired() default 3600;
}
