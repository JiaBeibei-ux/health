package com.haowaner.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.haowaner.constant.MessageConstant;
import com.haowaner.constant.RedisConstant;
import com.haowaner.entity.Result;
import com.haowaner.pojo.Order;
import com.haowaner.service.OrderService;
import com.haowaner.utils.SMSUtils;
import com.haowaner.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 预约控制器
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/validateTel")
    public Result validateTel(@RequestParam(value="tel") String tel){
        String code4String = ValidateCodeUtils.generateValidateCode(4).toString();
        //发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,tel,code4String);
            //放到redis中减轻数据库的压力
            jedisPool.getResource().setex(tel+ RedisConstant.SENDTYPE_ORDER,5*60*60,code4String);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    /**
     * 根据id查询 预约人员
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findByid(@RequestParam(value="id") Integer id){
        try {
            Order order = orderService.findByid(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,order);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }

    /**
     * 预约人员提交信息
     * @param map
     * @return
     */
    @RequestMapping("/sumbit")
    public Result submit(@RequestBody Map map){
        String telephone = (String)map.get("telephone");
        //比对验证码
        String codeInRedis = jedisPool.getResource().get(telephone + RedisConstant.SENDTYPE_ORDER);
        if(codeInRedis==null || !codeInRedis.equals(map.get("validateCode"))){
            return  new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        //调用体检服务 预约方法
        Result result = null;
        try {
            result = orderService.order(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
        //如果上面成功调用方法 就发送验证码
        if(result.isFlag()){
            String orderDate = (String)map.get("orderDate");
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderDate);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
