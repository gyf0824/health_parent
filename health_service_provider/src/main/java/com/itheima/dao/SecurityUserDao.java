package com.itheima.dao;

import com.itheima.pojo.User;

public interface SecurityUserDao {
    User findByUsername(String username);
}
