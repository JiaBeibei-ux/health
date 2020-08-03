package com.haowaner.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.haowaner.constant.MessageConstant;
import com.haowaner.constant.RedisConstant;
import com.haowaner.entity.Result;
import com.haowaner.pojo.Member;
import com.haowaner.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * 成员控制器
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    /**
     * 1、判断验证码是否正确
     * 2、判断用户是否为会员
     * 3、将信息保存到cookie中，
     * 4、会员信息保存到redis代替了session
     * 快速登录的方法
     * @param map 使用map来接受前端传来的参数
     * @return
     */
    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response){
        String tel = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //比对验证码是否正确
        String validateCodeInRedis = jedisPool.getResource().get(tel + RedisConstant.SENDTYPE_LOGIN);
        if(validateCode==null || !validateCode.equals(validateCodeInRedis)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //判断用户是否为会员
        Member member = memberService.findByTel(tel);
        if(member==null){
            //如果用户不是会员 自动注册
            member = new Member();
            member.setPhoneNumber(tel);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //登录成功 写入Cookie 保存用户信息 并返回
        Cookie cookie = new Cookie("login_member_telephone", tel);
        cookie.setPath("/");//这个是浏览器发送请求时都会携带
        cookie.setMaxAge(30*24*60*60);//有效期
        //将会员信息存到redis中
        String json = JSONObject.toJSON(member).toString();
        jedisPool.getResource().setex(tel,60*60,json);
        return new Result(true,MessageConstant.LOGIN_SUCCESS);
    }
}
