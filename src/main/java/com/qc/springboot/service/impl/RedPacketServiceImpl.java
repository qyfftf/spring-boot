package com.qc.springboot.service.impl;

import com.qc.springboot.common.constants.RedPacketConstants;
import com.qc.springboot.common.dto.R;
import com.qc.springboot.common.utils.IdWorker;
import com.qc.springboot.common.utils.RedisUtils;
import com.qc.springboot.entities.RedPacketRecord;
import com.qc.springboot.service.RedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author qc
 * @create 2020-02-22 11:12
 */
@Service
@Slf4j
public class RedPacketServiceImpl implements RedPacketService {
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    RedisUtils redisUtils;
    @Autowired
    IdWorker idWorker;
    @Override
    public Integer getRedPacket(String redPacketId,String userId) {
        Integer money=0;
        RLock lock=null;
        boolean flag;
        try{
            lock = redissonClient.getLock(redPacketId + "");
            flag = lock.tryLock(3, 5, TimeUnit.SECONDS);
            if(flag){
                long restPeople=redisUtils.decr(redPacketId+ RedPacketConstants.RESTPEOPLE,1);
                if(restPeople>=0){
                    if(restPeople==0){//如果是最后一人
                        money=Integer.parseInt(redisUtils.get(redPacketId+RedPacketConstants.MONEY));
                    }else {
                        Integer restMoney=Integer.parseInt(redisUtils.get(redPacketId+RedPacketConstants.MONEY));
                        Random random=new Random();
                        money=random.nextInt((int)(restMoney/(restPeople+1)*2-1))+1;
                    }
                }else {
                    money=0;
                }
            }
        }catch(Exception e){
            log.info("抢红包失败，金额，人数发生回滚");
            redisUtils.incr(redPacketId+":restPeople",1);
            money=-1;
            e.printStackTrace();
        }finally {
            if(money!=-1){
                redisUtils.decr(redPacketId+":money",money);
                RedPacketRecord redPacketRecord=new RedPacketRecord();
                redPacketRecord.setId(idWorker.nextId());
                redPacketRecord.setCreateTime(new Timestamp(new Date().getTime()));
                redPacketRecord.setMoney(money);
                redisUtils.hset(RedPacketConstants.REDPACKETRECORD,userId,redPacketRecord);
            }
            lock.unlock();
        }
        return money;
    }
}
