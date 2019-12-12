package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.pojo.*;
import com.itheima.utils.ExcelUtil;
import com.itheima.MemberService_gyf;
import com.itheima.dao.MemberDao_gyf;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;


import com.itheima.utils.MD5Utils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 会员服务
 */
@Service(interfaceClass = MemberService_gyf.class)
@Transactional
public class MemberServiceImpl_gyf implements MemberService_gyf {
    @Autowired
    private MemberDao_gyf memberDao;

    //根据手机号查询会员
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    //新增会员
    @Override
    public void add(Member member) {
        if(member.getPassword() != null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> list = new ArrayList<>();
        for (String month : months) {
            month  = month + ".31";
            Integer count = memberDao.findMemberCountBeforeDate(month);
            list.add(count);
        }
        return list;
    }

    @Override
    public PageResult selectByCondition(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();//查询条件
        //完成分页查询，基于mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);
        //select * from t_member limit 0,10
        Page<Member> page = memberDao.selectByCondition(queryString);
        System.out.println(page);
        long total = page.getTotal();
        List<Member> rows = page.getResult();
        int pageNum = page.getPageNum();
        return new PageResult(total,rows);
    }

    @Override
    public void deleteById(Integer id) {
        memberDao.deleteById(id);
    }

    // 修改会员项数据
    @Override
    public void edit(Member member) {
        memberDao.edit(member);
    }

    //根据ID回显编辑会员窗口
    @Override
    public Member findById(Integer id) {
        return memberDao.findById(id);
    }

    @Override
    public List<Member> findAll() {
        return memberDao.findAll();
    }

    //导入excel文件
    @Override
    public void importExcel(File file, String excelFileName) {
        // TODO Auto-generated method stub
        //1.创建输入流
        try {
            FileInputStream inputStream = new FileInputStream(file);
            boolean is03Excel = excelFileName.matches("^.+\\.(?i)(xls)$");
            //1.读取工作簿
            Workbook workbook = is03Excel?new HSSFWorkbook(inputStream):new XSSFWorkbook(inputStream);
            //2.读取工作表
            Sheet sheet = workbook.getSheetAt(0);
            //3.读取行
            //判断行数大于三,是因为数据从第四行开始插入
            if(sheet.getPhysicalNumberOfRows() > 3)
            {
                Member member = null;
                //跳过前两行
                for(int k=2;k<sheet.getPhysicalNumberOfRows();k++ )
                {
                    //读取单元格
                    Row row0 = sheet.getRow(k);
                    member = new Member();
                    //档案号
                    Cell cell0 = row0.getCell(0);
                    member.setFileNumber(cell0.getStringCellValue());
                    //姓名
                    Cell cell1 = row0.getCell(1);
                    member.setName(cell1.getStringCellValue());
                    //设置性别
                    Cell cell2 = row0.getCell(2);
                    member.setSex(cell2.getStringCellValue());
                    //身份证
                    Cell cell3 = row0.getCell(3);
                    member.setIdCard(cell3.getStringCellValue());
                    //设置手机号码
                    String phoneNumber = "";
                    Cell cell4 = row0.getCell(4);
                    try {
                        phoneNumber = cell4.getStringCellValue();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        double dphoneNumber = cell4.getNumericCellValue();
                        phoneNumber = BigDecimal.valueOf(dphoneNumber).toString();
                    }
                    member.setPhoneNumber(phoneNumber);
                    //注册时间
                    Cell cell5 = row0.getCell(5);
                    member.setRegTime(cell5.getDateCellValue());
                    //设置电子邮箱
                    Cell cell6 = row0.getCell(6);
                    member.setEmail(cell6.getStringCellValue());
                    //出生日期
                    Cell cell7 = row0.getCell(7);
                    member.setBirthday(cell7.getDateCellValue());
                    //备注
                    Cell cell8 = row0.getCell(8);
                    member.setEmail(cell8.getStringCellValue());


                }
            }
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void exportExcel(List<Member> memberList, ServletOutputStream out) {
        // TODO Auto-generated method stub
        ExcelUtil.exportMemberExcel(memberList, out);
    }

    @Override
    public List<Member> findObjects(Integer[] memberIds) {
        List<Member> list = new ArrayList<>();
        for (Integer memberId : memberIds) {
            Member member = memberDao.findByMemberId(memberId);
            list.add(member);
        }
        return list;

    }

    @Override
    public Integer[] findByIds() {
        return memberDao.findByIds();
    }

    @Override
    public List<Order> findOrderByMemberId(Integer id) {
        List<Order> orderByMemberId = memberDao.findOrderByMemberId(id);
        for (Order order : orderByMemberId) {
            Integer[] setmealid = memberDao.findSetmealId(id);
            for (Integer integer : setmealid) {
                order.setSetmealId(integer);
            }

        }
        return orderByMemberId;
    }

    @Override
    public List<Setmeal> findSetmealByOrderId(Integer setmealId) {
        return memberDao.findSetmealByOrderId(setmealId);
    }

    @Override
    public List<CheckGroup> findCheckGroupBySetmealId(Integer setmealId) {
        return memberDao.findCheckGroupBySetmealId(setmealId);
    }

    @Override
    public List<CheckItem> findCheckItemByCheckGroupId(Integer checkGroupId) {
        return memberDao.findCheckItemByCheckGroupId(checkGroupId);
    }

}
