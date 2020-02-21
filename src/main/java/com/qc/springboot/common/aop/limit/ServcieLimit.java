package com.qc.springboot.common.aop.limit;

import com.qc.springboot.common.enums.LimitType;

import java.lang.annotation.*;

/**
 * 自定义注解：限流
 * @author qc
 * @create 2020-02-19 9:35
 */
@Target({ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServcieLimit {
    /**
     * 描述
     */
    String description() default "";

    /**
     * key
     */
    String key() default "";

    /**
     * 类型
     */
    LimitType limitType() default LimitType.CUSTOMER;
}
