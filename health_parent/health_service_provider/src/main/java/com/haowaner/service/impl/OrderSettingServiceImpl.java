package com.haowaner.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.haowaner.constant.MessageConstant;
import com.haowaner.dao.OrderSettingDao;
import com.haowaner.entity.Result;
import com.haowaner.pojo.OrderSetting;
import com.haowaner.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预订服务接口实现类
 */
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    //预定设置 添加方法
    @Override
    public void add(List<OrderSetting> orderSettings) {
        //集合不为空
        if(orderSettings!=null && orderSettings.size()>0) {
            //遍历一下集合
            for (OrderSetting orderSetting : orderSettings) {
                //开始写一下具体的方法 跟具体的日期查询 如果查询结果大于0 就跟新预约人数 否则就插入
                Long counts = orderSettingDao.findByDate(orderSetting.getOrderDate());
                if(counts>0){
                    orderSettingDao.editByDate(orderSetting);
                }else {
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    //查询当前月的所有预约信息 date 例如2020-06
    @Override
    public List<Map> findNumberByMonth(String date) {
        //做一下简单的处理
        String dateBegin = date+"-01";
        String dateEnd = date+"-31";
        Map<String,String> map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
        List<OrderSetting> numberByMonths = orderSettingDao.findNumberByMonth(map);
        //将查询出出来的信息封装到List
        List<Map> data = new ArrayList<>();
        if(numberByMonths!=null && numberByMonths.size()>0){
            //将查询出来的信息遍历一下 封装到map中
            for (OrderSetting numberByMonth : numberByMonths) {
                Map orderSettingsMap = new HashMap();
                orderSettingsMap.put("date",numberByMonth.getOrderDate().getDate());
                orderSettingsMap.put("number",numberByMonth.getNumber());
                orderSettingsMap.put("reservations",numberByMonth.getReservations());
                data.add(orderSettingsMap);
            }
        }
        return data;
    }

    //根据具体的日期去更新
    @Override
    public void editByDate(OrderSetting orderSetting) {
       orderSettingDao.editByDate(orderSetting);
    }

}
