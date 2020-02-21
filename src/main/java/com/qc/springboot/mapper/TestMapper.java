package com.qc.springboot.mapper;

import com.qc.springboot.entities.Test;
import org.apache.ibatis.annotations.Param;

/**
 * @author qc
 * @create 2020-02-20 17:42
 */

public interface TestMapper {
    Test getByid(Integer id);

    void updateById(@Param("id") int id,@Param("name") String name);
}
