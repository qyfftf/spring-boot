package com.qc.springboot.web.limitip;

import com.qc.springboot.common.aop.limit.ServcieLimit;
import com.qc.springboot.common.dto.R;
import com.qc.springboot.common.enums.LimitType;
import com.qc.springboot.common.utils.RedisUtils;
import com.qc.springboot.entities.Test;
import com.qc.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**测试限流
 * @author qc
 * @create 2020-02-19 10:32
 */
@RestController
@RequestMapping("/limit")
public class TestController {
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    TestService testService;
    @GetMapping("/test1")
    @ServcieLimit(limitType = LimitType.IP)
    public R test1(){
        R r = R.ok();
        Test byId = testService.getById(2);
        r.setMessage("访问成功"+byId);
        return r;
    }
    @GetMapping("/test2")
    public R test2(){
        R r = R.ok();
        testService.updateById(2,"小红");
        r.setMessage("访问成功");
        return r;
    }
}
