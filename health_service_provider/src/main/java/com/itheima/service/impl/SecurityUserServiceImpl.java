package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.SecurityUserService;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.SecurityUserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 *
 * 用户服务
 */
@Service(interfaceClass = SecurityUserService.class)
@Transactional
public class SecurityUserServiceImpl implements SecurityUserService {
    @Autowired
    private SecurityUserDao securityUserDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    //根据用户名查询数据库获取用户信息和关联的角色信息，同时需要查询角色相关的权限信息
    @Override
    public User findByUsername(String username) {
        //查询用户基本信息，不包含用户的角色
        User user = securityUserDao.findByUsername(username);
        if(user == null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户id查询相对应的角色
        Set<Role> roles = roleDao.findByUserId(userId);
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色id查询相对应的权限
            Set<Permission> permissions = permissionDao.findByRoleId(roleId);
            if(permissions != null && permissions.size() >0){
                //让角色关联权限
                role.setPermissions(permissions);
            }

        }
        user.setRoles(roles);
        return user;
    }
}
