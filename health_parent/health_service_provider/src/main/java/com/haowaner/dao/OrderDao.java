package com.haowaner.dao;

import com.haowaner.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 预约持久层接口
 */
@Repository
public interface OrderDao {
    //查询预约信息
    public List<Order> findByCondition(Order order);

    //将预约信息保存起来
    public void add(Order order);

    //通过id查询
    public Order findByid(Integer id);

    //今日预约数
    public Integer findOrderCountByDate(String today);

    //本周预约数
    public Integer findOrderCountAfterDate(String thisWeekMonday);

    //今日到诊数
    public Integer findVisitsCountByDate(String today);

    //本周到诊数
    public Integer findVisitsCountAfterDate(String thisWeekMonday);

    //热门套餐
    public List<Map> findHotSetmeal();

}
