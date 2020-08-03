package com.haowaner.dao;

import com.haowaner.pojo.Role;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 角色持久层接口
 */
@Repository
public interface RoleDao {
   //去中间表里查询
   public Set<Role> findByUserId(Integer id);
}
