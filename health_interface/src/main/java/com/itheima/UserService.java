package com.itheima;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.User;

import java.util.List;

public interface UserService {
    void add(User user);

    PageResult selectByCondition(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    void edit(User user);

    User findById(Integer id);

    List<User> findAll();
}

