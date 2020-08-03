package com.haowaner.controller;

import com.aliyuncs.exceptions.ClientException;
import com.haowaner.constant.MessageConstant;
import com.haowaner.constant.RedisConstant;
import com.haowaner.entity.Result;
import com.haowaner.utils.SMSUtils;
import com.haowaner.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/validateCode")
public class validateCodeController {

    @Autowired
    private JedisPool jedisPool;
    /**
     * 快速登录验证码 6位的
     * @param
     * @return
     */
    @RequestMapping("/send4Login")
    public Result validateLogin(@RequestParam(value="telephone") String telephone){
        //调用工具类
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(SMSUtils.LOGIN_NOTICE,telephone,validateCode.toString());
            //将验证码存到redis
            jedisPool.getResource().setex(telephone+ RedisConstant.SENDTYPE_LOGIN,30*60,validateCode.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
