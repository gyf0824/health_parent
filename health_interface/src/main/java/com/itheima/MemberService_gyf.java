package com.itheima;

import com.itheima.pojo.*;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.util.List;

public interface MemberService_gyf {
    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonths(List<String> months);

    PageResult selectByCondition(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    void edit(Member member);

    Member findById(Integer id);

    List<Member> findAll();

    void importExcel(File file, String excelFileName);

    void exportExcel(List<Member> memberList, ServletOutputStream out);

    List<Member> findObjects(Integer[] memberIds);

    Integer[] findByIds();

    List<Order> findOrderByMemberId(Integer id);


    List<Setmeal> findSetmealByOrderId(Integer setmealId);

    List<CheckGroup> findCheckGroupBySetmealId(Integer setmealId);

    List<CheckItem> findCheckItemByCheckGroupId(Integer checkGroupId);
}
