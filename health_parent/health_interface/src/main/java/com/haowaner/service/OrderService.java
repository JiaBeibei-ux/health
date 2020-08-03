package com.haowaner.service;

import com.haowaner.entity.Result;
import com.haowaner.pojo.Order;

import java.util.Map;

/**
 * 预约服务接口
 */
public interface OrderService {
    //预约方法
    public Result order(Map map) throws Exception;

    //通过预约id查询
    public Order findByid(Integer id);
}
