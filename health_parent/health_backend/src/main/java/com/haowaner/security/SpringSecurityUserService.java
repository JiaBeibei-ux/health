package com.haowaner.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.haowaner.pojo.Permission;
import com.haowaner.pojo.Role;
import com.haowaner.pojo.User;
import com.haowaner.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference //使用dubbo远程调用服务
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       //连接数据库查询
        User user = userService.findByUsername(username);
        if(user==null){
            return null;
        }
        //创建一个list集合用来装权限的
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            //授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                //授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);
        return userDetails;
    }
}
