package com.haowaner.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.haowaner.dao.MemberDao;
import com.haowaner.constant.MessageConstant;
import com.haowaner.dao.OrderDao;
import com.haowaner.dao.OrderSettingDao;
import com.haowaner.entity.Result;
import com.haowaner.pojo.Member;
import com.haowaner.pojo.Order;
import com.haowaner.pojo.OrderSetting;
import com.haowaner.service.OrderService;
import com.haowaner.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 预约服务接口实现类
 */
@Service(interfaceClass=OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    //预约方法 业务逻辑有点多
    /**
     * 1、检查用户所选择的项目预约日期是否进行了预约的设置，如果没有设置 则没有办法进行预约的
     * 2、检查用户所选择的预约日期是否已满 如果已约满  则无法进行预约
     * 3、检查用户是否重复预约了(同一个用户预约了同一个套餐),如果是重复预约，则无法再次预约
     * 4、检查当前用户是否为会员 如果是会员直接完成预约，如果不是会员则自动完成注册且进行预约
     * 5、预约成功，更新当日已预约的人数
     * @param map
     * @return
     */
    @Override
    public Result order(Map map) throws Exception{
        Date date = DateUtils.parseString2Date((String)map.get("orderDate"));
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        //检查用户所选择的项目预约日期是否进行了预约的设置，如果没有设置 则没有办法进行预约的
        if(orderSetting==null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //预约日期人数已经预约已满
        int number = orderSetting.getNumber(); //可预约的人数
        int reservations = orderSetting.getReservations();//已预约的人数
        if(reservations>=number){
            //已约满
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //判断是否为会员
        String telephone = (String) map.get("telephone");
        //通过手机号判断 用用户是否为会员 会员直接预约
        Member member = memberDao.findByTel(telephone);
        //用户否为会员
        if(member!=null){
            Integer memberId = member.getId();//会员的id
            int setmealId = Integer.parseInt((String) map.get("setmealId"));//套餐的id
            Order order = new Order(memberId,date,null,null,setmealId);
            List<Order> orders = orderDao.findByCondition(order);
            if(orders!=null && orders.size()>0){
                //已经预约过了 不能重复预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        if(member==null){
            //当前用户不是会员
            member = new Member();
            member.setName((String)map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String)map.get("idCard"));
            member.setSex((String)map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
        //保存预约信息到预约表
        Order order = new Order(member.getId(),date,(String)map.get("orderType"), Order.ORDERSTATUS_NO,Integer.parseInt((String)map.get("setmealId")));
        orderDao.add(order);
        //可以预约设置预约人数
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //通过id查询
    @Override
    public Order findByid(Integer id) {
        return orderDao.findByid(id);
        //new HashSet<>()
    }
}
