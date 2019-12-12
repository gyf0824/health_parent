package com.itheima;

import com.itheima.pojo.CheckItem;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;

import java.util.List;

public interface CheckItemService {
    void add(CheckItem checkItem);

    PageResult selectByCondition(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    public void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
