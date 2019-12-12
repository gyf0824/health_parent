package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.*;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface MemberDao_gyf {
    @Select("select * from t_member")
    List<Member> findAll();

    Page<Member> selectByCondition(String queryString);
    void add(Member member);
    void deleteById(Integer id);
    Member findById(Integer id);
    Member findByTelephone(String telephone);
    void edit(Member member);
    Integer findMemberCountBeforeDate(String date);
    Integer findMemberCountByDate(String date);
    Integer findMemberCountAfterDate(String date);
    Integer findMemberTotalCount();

    Member findByMemberId(Integer memberId);

    Integer[] findByIds();

    Map<String,Object> findMemberAndSeteamlAndCheckGroupAndCheckItem(Integer memberId);

    List<Order> findOrderByMemberId(Integer id);

    List<Setmeal> findSetmealByOrderId(Integer setmealId);

    List<CheckGroup> findCheckGroupBySetmealId(Integer setmealId);

    List<CheckItem> findCheckItemByCheckGroupId(Integer checkGroupId);

    Integer[] findSetmealId(Integer id);
}
