package com.haowaner.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.haowaner.dao.PermissionDao;
import com.haowaner.dao.RoleDao;
import com.haowaner.dao.UserDao;
import com.haowaner.pojo.Permission;
import com.haowaner.pojo.Role;
import com.haowaner.pojo.User;
import com.haowaner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 用户服务实现类
 */
@Service(interfaceClass=UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    //逻辑有点复杂 这里和userDetail一样
    @Override
    public User findByUsername(String username) {
        User user = userDao.findByUsername(username);
        if(user==null){
            return null;
        }
        //用户id
        Integer userId = user.getId();
        //用户拥有的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        if(roles!=null && roles.size()>0){
            for (Role role : roles) {
                Integer roleId = role.getId();
                //角色所拥有的权限
                Set<Permission> permissions = permissionDao.findByRoleId(roleId);
                if(permissions!=null && permissions.size()>0){
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }
}
