package com.haowaner.dao;

import com.haowaner.pojo.User;
import org.springframework.stereotype.Repository;

/**
 * 用户持久层接口
 */
@Repository
public interface UserDao {
    //通过姓名查询
    public User findByUsername(String username);
}
