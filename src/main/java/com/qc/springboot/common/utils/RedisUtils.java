package com.qc.springboot.common.utils;

import com.alibaba.fastjson.JSON;
import com.qc.springboot.common.enums.RedisType;
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
    //==============对reids自定义注解 切面的处理获取缓存业务操作封装============================
    public <T> T redisReadCache(String key, String feild, RedisType redisType,Class<T> clazz){
        if(redisType.equals(RedisType.STRING)){
            return get(key,clazz);
        }else if(redisType.equals(RedisType.HASH)){
            return hget(key,feild,clazz);
        }else if(redisType.equals(RedisType.LIST)){
            //TODO 对list类型的数据进行处理
        }else if(redisType.equals(RedisType.SET)){
            //TODO 对set类型的数据进行处理
        }else if(redisType.equals(RedisType.ZSET)){
            //TODO 对zset类型的数据进行处理
        }
        return null;
    }
    //==============对reids自定义注解 切面的处理获取缓存业务操作封装============================
    public void redisWriteCache(String key, String feild,
                                RedisType redisType,Object object,
                                long expire){
        if(redisType.equals(RedisType.STRING)){
            set(key,object,expire);
        }else if(redisType.equals(RedisType.HASH)){
            hset(key,feild,object);
        }else if(redisType.equals(RedisType.LIST)){
            //TODO 对list类型的数据进行处理
        }else if(redisType.equals(RedisType.SET)){
            //TODO 对set类型的数据进行处理
        }else if(redisType.equals(RedisType.ZSET)){
            //TODO 对zset类型的数据进行处理
        }
    }
    //==============对reids自定义注解 切面的处理清除缓存业务操作封装============================
    public void redisDelteCache(String key,String feild,
                                RedisType redisType){
        if(redisType.equals(RedisType.STRING)){
            delete(key);
        }else if(redisType.equals(RedisType.HASH)){
            hdel(key,feild);
        }else if(redisType.equals(RedisType.LIST)){
            //TODO 对list类型的数据进行处理
        }else if(redisType.equals(RedisType.SET)){
            //TODO 对set类型的数据进行处理
        }else if(redisType.equals(RedisType.ZSET)){
            //TODO 对zset类型的数据进行处理
        }
    }

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
    /*redis的递增与递减*/

    /**
     *
     * @param key 操作的key
     * @param l 递增因子
     */
    public long incr(String key,long l){
        return valueOperations.increment(key, l);
    }

    /**
     *
     * @param key
     * @param l 递减因子
     */
    public long decr(String key,long l){
        return valueOperations.decrement(key,l);
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
