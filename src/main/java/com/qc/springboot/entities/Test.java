package com.qc.springboot.entities;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qc
 * @create 2020-02-20 17:40
 */
@Data
public class Test implements Serializable {
    private Integer id;
    private String name;
}
