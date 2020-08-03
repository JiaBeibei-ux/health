package com.haowaner.dao;

import com.haowaner.pojo.OrderSetting;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预定设置持久层接口
 */
@Repository
public interface OrderSettingDao {
    //添加
    public void add(OrderSetting orderSetting);

    //根据具体的日期去查询
    public Long findByDate(Date orderDate);

    //根据具体的日期去更新预约人数
    public void editByDate(OrderSetting orderSetting);

    //查询出当前月所有的预约信息
    public List<OrderSetting> findNumberByMonth(Map<String, String> map);

    //根据预约的日期查询
    public OrderSetting findByOrderDate(Date date);

    //更新预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
