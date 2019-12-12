package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.User;

import java.util.List;

public interface UserDao {
    void add(User user);

    Page<User> selectByCondition(String queryString);

    long findCountByUserId(Integer id);

    void deleteAssocication(Integer id);

    void deleteById(Integer id);

    void edit(User user);

    User findById(Integer id);

    List<User> findAll();
}
