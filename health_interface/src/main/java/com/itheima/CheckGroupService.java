package com.itheima;

import com.itheima.pojo.CheckGroup;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult queryPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    List<CheckGroup> findAll();

    void deleteById(Integer id);
}
