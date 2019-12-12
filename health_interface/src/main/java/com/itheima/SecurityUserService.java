package com.itheima;

import com.itheima.pojo.User;

/**
 * 用户服务接口
 */
public interface SecurityUserService {
    User findByUsername(String username);
}
