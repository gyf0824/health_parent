package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.CheckItemService;
import com.itheima.UserService;
import com.itheima.dao.CheckItemDao;
import com.itheima.dao.UserDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    //注入dao
    @Autowired
    private UserDao userDao;
    //新增
    @Override
    public void add(User user) {
        userDao.add(user);
    }

    //用户分页查询
    @Override
    public PageResult selectByCondition(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_checkitem limit 0,10
        Page<User> page = userDao.selectByCondition(queryString);
        System.out.println(page);
        long total = page.getTotal();
        List<User> rows = page.getResult();
        int pageNum = page.getPageNum();
        return new PageResult(total,rows);
    }


    //根据ID删除用户信息

    @Override
    public void deleteById(Integer id) {
        //判断当前用户是否已经关联到角色
        long count = userDao.findCountByUserId(id);
        if(count > 0){
            //先删除关联表的关系，再删除t_role表
            userDao.deleteAssocication(id);
            //当前用户已经被关联到角色，不允许删除
//            new RuntimeException();
        }
        //没有与其关联的数据就直接删除
        userDao.deleteById(id);
    }

    //修改用户数据
    @Override
    public void edit(User user) {
        userDao.edit(user);
    }

    //根据ID回显编辑检查窗口
    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
