package com.haowaner.service;

import com.haowaner.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * 预定设置业务层接口
 */
public interface OrderSettingService {

  //预定设置 添加方法
   public void add(List<OrderSetting> orderSettings);

   //查询当前月的所有预约信息
   public List<Map> findNumberByMonth(String date);

   //根据具体的日期去更新预约人数
   public void editByDate(OrderSetting orderSetting);

}
