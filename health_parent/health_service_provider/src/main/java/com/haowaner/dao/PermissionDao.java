package com.haowaner.dao;

import com.haowaner.pojo.Permission;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * 权限持久层
 */
@Repository
public interface PermissionDao {
    //通过角色id查询权限
    public Set<Permission> findByRoleId(Integer roleId);

}
