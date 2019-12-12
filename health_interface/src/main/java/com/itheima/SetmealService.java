package com.itheima;

import com.itheima.pojo.Setmeal;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    PageResult findPage(QueryPageBean queryPageBean);

    void add(Setmeal setmeal, Integer[] checkgroupIds);

    void deleteById(Integer id);

    void edit(Setmeal setmeal, Integer[] checkgroupIds);

    List<Setmeal> findAll();

    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
