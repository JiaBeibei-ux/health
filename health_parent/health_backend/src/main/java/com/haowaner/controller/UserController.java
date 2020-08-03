package com.haowaner.controller;

import com.haowaner.constant.MessageConstant;
import com.haowaner.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername()throws Exception{
        try {
            //Spring框架借助ThreadLocal来保存和传递用户登录信息,来获取保存在ThreadLocal中的用户信息。
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
