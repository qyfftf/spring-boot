package com.qc.springboot.web.redpacket;


import com.qc.springboot.common.dto.R;
import com.qc.springboot.service.RedPacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**抢红包
 * @author qc
 * @create 2020-02-22 11:08
 */
@RestController
public class RedPacketController {
    @Autowired
    RedPacketService redPacketService;
    /**
     * 抢红包
     * @return
     */
    @GetMapping("/redpacket")
    public R getRedPacket(String userId, String redPacketId){
        Integer redPacket = redPacketService.getRedPacket(redPacketId, userId);
        if(redPacket==0){
            return R.ok().message("手慢了，红包已经抢完了");
        }else if(redPacket==-1){
            return R.error().message("发生异常");
        }
        return R.ok().data("用户"+userId,redPacket+"元");
    }
}
