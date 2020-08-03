package com.haowaner.service;

import com.haowaner.pojo.User;

/**
 * 用户服务接口
 */
public interface UserService {

    //权限  通过姓名查询
    public User findByUsername(String username);
}
