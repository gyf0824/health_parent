package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    Page<Setmeal> selectByCondition(String queryString);

    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(Map<String,Integer> map);

    long findCountBySetmealId(Integer id);

    void deleteAssocication(Integer id);

    void deleteById(Integer id);

    void edit(Setmeal setmeal);

    List<Setmeal> findAll();

    List<Integer> findCheckGroupIdsBySetmealId(Integer id);

    Setmeal findById(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
