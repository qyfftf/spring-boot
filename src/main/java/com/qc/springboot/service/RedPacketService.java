package com.qc.springboot.service;

import com.qc.springboot.common.dto.R;

/**
 * @author qc
 * @create 2020-02-22 11:11
 */
public interface RedPacketService {
    Integer getRedPacket(String redPacketId, String userId);
}
