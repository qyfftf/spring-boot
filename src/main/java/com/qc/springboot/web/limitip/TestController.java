package com.qc.springboot.web.limitip;

import com.qc.springboot.common.aop.ServcieLimit;
import com.qc.springboot.common.dto.R;
import com.qc.springboot.common.enums.LimitType;
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
    @GetMapping("/test")
    @ServcieLimit(limitType = LimitType.IP)
    public R test(){
        R r = R.ok();
        r.setMessage("访问成功");
        return r;
    }
}
