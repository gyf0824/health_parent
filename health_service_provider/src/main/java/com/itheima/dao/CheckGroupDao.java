package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(Map<String, Integer> map);

    Page<CheckGroup> selectByCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

    void edit(CheckGroup checkGroup);

    void deleteAssocication(Integer id);

    List<CheckGroup> findAll();

    @Select("select count(*) from t_checkgroup_checkitem where checkgroup_id = #{checkgroup_id}")
    long findCountByCheckGroupId(Integer id);

    void deleteById(Integer id);
}


