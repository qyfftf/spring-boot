package com.qc.springboot.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**封装的redis工具类
 * @author qc
 * @create 2020-02-20 11:52
 */
@Component
public class RedisUtils {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    /**
     * 操作kv
     */
    @Autowired
    private ValueOperations<String,String> valueOperations;
    @Autowired
    private HashOperations<String,String,String> hashOperations;
    /**
     * 默认过期时长，单位：秒
     */
    private final  static long DEFALUT_EXPIRE=60*60*24;
    /**
     * 不设置过期时长
     */
    public final static long NO_EXPIRE=-1;

    /**
     * 插入缓存默认时间
     * @param key
     * @param value
     */
    public void set(String key,Object value){
        set(key,value,DEFALUT_EXPIRE);
    }

    /**
     * 返回字符串结果
     * @param key
     * @return
     */
    public String get(String key){
        return valueOperations.get(key);
    }

    /**
     * 返回指定类型结果
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key,Class<T> clazz){
        String value = valueOperations.get(key);
        return value==null?null:fromJson(value,clazz);
    }

    /**
     * 删除key
     * @param key
     */
    public void delete(String key){
        redisTemplate.delete(key);
    }

    /**
     * 插入缓存指定时间
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key,Object value,long expire){
        valueOperations.set(key,toJson(value));
        redisTemplate.expire(key,expire, TimeUnit.SECONDS);
    }
    /*---------------------hash-----------------------------*/

    /**
     * hash数据类型的插入 默认时间
     * @param key
     * @param feild
     * @param object
     */
    public void hset(String key,String feild,Object object){
        hashOperations.put(key,feild,toJson(object));
    }


    /**
     * 获取值
     * @param key
     * @param feild
     * @return
     */
    public String hget(String key,String feild){
        return hashOperations.get(key,feild);
    }
    /**
     * 获取值
     * @param key
     * @param feild
     * @return
     */
    public <T> T hget(String key,String feild,Class<T> clazz){
        return fromJson(hashOperations.get(key,feild),clazz);
    }

    /**
     * 删除hash中指定的key
     * @param key
     * @param feild
     */
    public void hdel(String key,String feild){
        hashOperations.delete(key,feild);
    }

    /**
     * 删除hash的key
     * @param key
     */
    public void hdel(String key){
        redisTemplate.delete(key);
    }
    /**
     * object转json
     * @return
     */
    private String toJson(Object object){
        if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double
                || object instanceof Boolean || object instanceof String)
        {
            return String.valueOf(object);
        }
        return JSON.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz)
    {
        return JSON.parseObject(json, clazz);
    }
}
