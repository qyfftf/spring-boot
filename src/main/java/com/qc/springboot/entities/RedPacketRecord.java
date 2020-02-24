package com.qc.springboot.entities;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 抢红包记录表
 */
@Data
public class RedPacketRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long redPacketId;
    private Integer money;
    private Integer uid;
    private Timestamp createTime;//创建时间

}
