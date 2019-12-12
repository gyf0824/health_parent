package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.MemberService_gyf;
import com.itheima.constant.MessageConstant;
import com.itheima.pojo.*;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.utils.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController_gyf {
    @Reference
    private MemberService_gyf memberService;

    //新增服务项
    @RequestMapping("/add")
    public Result add(@RequestBody Member memeber){
        try{
            memberService.add(memeber);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MEMBER_FAIL);
        }
        return new Result(true,MessageConstant.ADD_MEMBER_SUCCESS);
    }

    //检查项分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = memberService.selectByCondition(queryPageBean);
        return pageResult;
    }

    //删除检查项
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try{
            memberService.deleteById(id);

        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.DELETE_MEMBER_FAIL);
        }
        return  new Result(true, MessageConstant.DELETE_MEMBER_SUCCESS);
    }
    //编辑窗口检查项
    @RequestMapping("/edit")
    public Result edit(@RequestBody Member member){
        try{
            memberService.edit(member);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.EDIT_MEMBER_FAIL);
        }
        return  new Result(true, MessageConstant.EDIT_MEMBER_SUCCESS);
    }
    //根据id回显编辑数据
    @RequestMapping("/findById")
    public Result findById(Integer id){
        Member member = null;
        try{
            member = memberService.findById(id);
            //打印一下
            System.out.println(member);
            String s = DateUtils.parseDate2String(member.getBirthday());
            member.setBirthday(DateUtils.parseString2Date(s));
            return new Result(true, MessageConstant.QUERY_MEMBER_SUCCESS,member);
        }catch (Exception e){
            e.printStackTrace();
            //服务调用失败
            return new Result(false, MessageConstant.QUERY_MEMBER_FAIL);
        }
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try{
            List<Member> members = memberService.findAll();

            return new Result(true,MessageConstant.QUERY_MEMBER_SUCCESS,members);
        }catch(Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_MEMBER_FAIL);
        }
    }

    //批量导出会员信息数据
    @RequestMapping("/exportExcel")
    public void exportExcel(Integer[] memberIds, HttpServletResponse response, HttpServletRequest request)
    {
        System.out.println(Arrays.toString(memberIds));
        try
        {

            //1.查找会员基本信息
            List<Member> memberList =  memberService.findObjects(memberIds);

            System.out.println(memberList);
            System.out.println("- - - - -");
            String filePath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"member_template.xlsx";
            //基于提供的Excel模板文件在内存中创建一个Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            System.out.println(excel);
            System.out.println("- - - - - -");
            //读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);
            System.out.println(sheet);
            System.out.println("- - - - - -");
            //4.操作单元格;将用户列表写入excel
            System.out.println(memberList.get(0));
            System.out.println("- - - - - -");
            if(memberList != null)
            {

                    for(int j=0;j<memberList.size();j++){
                        XSSFRow row1 = sheet.getRow(2);
                        XSSFCell cell1 = row1.getCell(j + 7);
                        if(cell1 == null){  cell1 = row1.createCell(j+7);  }
                        cell1.setCellValue(memberList.get(j).getFileNumber());//档案号

                        XSSFRow row2 = sheet.getRow(3);
                        XSSFCell cell2 = row2.getCell(j+7);
                        if(cell2 == null){  cell2 = row2.createCell(j+7);  }
                        cell2.setCellValue(memberList.get(j).getName());//姓名

                        XSSFRow row3 = sheet.getRow(4);
                        XSSFCell cell3 = row3.getCell(j+7);
                        if(cell3 == null){  cell3 = row3.createCell(j+7);  }
                        cell3.setCellValue(memberList.get(j).getSex());//性别

                        XSSFRow row4 = sheet.getRow(5);
                        XSSFCell cell4 = row4.getCell(j+7);
                        if(cell4 == null){  cell4 = row4.createCell(j+7);  }
                        cell4.setCellValue(memberList.get(j).getIdCard());//身份证

                        XSSFRow row5 = sheet.getRow(6);
                        XSSFCell cell5 = row5.getCell(j+7);
                        if(cell5 == null){  cell5 = row5.createCell(j+7);  }
                        cell5.setCellValue(memberList.get(j).getPhoneNumber());//手机号

                        XSSFRow row6 = sheet.getRow(7);
                        XSSFCell cell6 = row6.getCell(j+7);
                        if(cell6 == null){  cell6 = row6.createCell(j+7);  }
                        cell6.setCellValue(DateUtils.parseDate2String(memberList.get(j).getRegTime()));//注册时间

                        XSSFRow row7 = sheet.getRow(8);
                        XSSFCell cell7 = row7.getCell(j+7);
                        if(cell7 == null){  cell7 = row7.createCell(j+7);  }
                        cell7.setCellValue(memberList.get(j).getEmail());//邮箱

                        XSSFRow row8 = sheet.getRow(9);
                        XSSFCell cell8 = row8.getCell(j+7);
                        if(cell8 == null){  cell8 = row8.createCell(j+7);  }
                        cell8.setCellValue(DateUtils.parseDate2String(memberList.get(j).getBirthday()));//出生日期

                        XSSFRow row9 = sheet.getRow(10);
                        XSSFCell cell9 = row9.getCell(j+7);
                        if(cell9 == null){  cell9 = row9.createCell(j+7);  }
                        cell9.setCellValue(memberList.get(j).getRemark());//备注

                        //根据会员id获取预约订单表
                        List<Order> listOrder = memberService.findOrderByMemberId(memberList.get(j).getId());
                        System.out.println(Arrays.toString(listOrder.toArray()));
                        for (int i = 0; i < listOrder.size(); i++) {
                            XSSFRow row10 = sheet.getRow(11);
                            XSSFCell cell10 = row10.getCell(j+7);
                            if(cell10 == null){  cell10 = row10.createCell(j+7);  }
                            cell10.setCellValue(DateUtils.parseDate2String(listOrder.get(i).getOrderDate()));//预约时间

                            XSSFRow row11 = sheet.getRow(12);
                            XSSFCell cell11 = row11.getCell(j+7);
                            if(cell11 == null){  cell11 = row11.createCell(j+7);  }
                            cell11.setCellValue(listOrder.get(i).getOrderType());//预约方式

                            XSSFRow row12 = sheet.getRow(13);
                            XSSFCell cell12 = row12.getCell(j+7);
                            if(cell12 == null){  cell12 = row12.createCell(j+7);  }
                            cell12.setCellValue(listOrder.get(i).getOrderStatus());//是否到诊
                            System.out.println(listOrder.get(i).getSetmealId());
                            //根据订单号id获取套餐
                            List<Setmeal> listSetmeal = memberService.findSetmealByOrderId(listOrder.get(i).getSetmealId());
                            System.out.println(Arrays.toString(listSetmeal.toArray()));
                            for (int z = 0; z < listSetmeal.size(); z++) {
                                XSSFRow row13 = sheet.getRow(14);
                                XSSFCell cell13 = row13.getCell(j+7);
                                if(cell13 == null){  cell13 = row13.createCell(j+7);  }
                                cell13.setCellValue(listSetmeal.get(z).getName());//套餐名称

                                XSSFRow row14 = sheet.getRow(15);
                                XSSFCell cell14 = row14.getCell(j+7);
                                if(cell14 == null){  cell14 = row14.createCell(j+7);  }
                                cell14.setCellValue(listSetmeal.get(z).getPrice());//套餐价格

                                XSSFRow row15 = sheet.getRow(16);
                                XSSFCell cell15 = row15.getCell(j+7);
                                if(cell15 == null){  cell15 = row15.createCell(j+7);  }
                                cell15.setCellValue(listSetmeal.get(z).getCode());//套餐编码

                                XSSFRow row16 = sheet.getRow(17);
                                XSSFCell cell16 = row16.getCell(j+7);
                                if(cell16 == null){  cell16 = row16.createCell(j+7);  }
                                cell16.setCellValue(listSetmeal.get(z).getSex());//套餐适用性别


                                XSSFRow row17 = sheet.getRow(18);
                                XSSFCell cell17 = row17.getCell(j+7);
                                if(cell17 == null){  cell17 = row17.createCell(j+7);  }
                                cell17.setCellValue(listSetmeal.get(z).getAge());//套餐适用年龄

                                XSSFRow row18 = sheet.getRow(19);
                                XSSFCell cell18 = row18.getCell(j+7);
                                if(cell18 == null){  cell18 = row18.createCell(j+7);  }
                                cell18.setCellValue(listSetmeal.get(z).getHelpCode());//套餐助记码

                                XSSFRow row19 = sheet.getRow(20);
                                XSSFCell cell19 = row19.getCell(j+7);
                                if(cell19 == null){  cell19 = row19.createCell(j+7);  }
                                cell19.setCellValue(listSetmeal.get(z).getRemark());//套餐说明

                                XSSFRow row20 = sheet.getRow(21);
                                XSSFCell cell20 = row20.getCell(j+7);
                                if(cell20 == null){  cell20 = row20.createCell(j+7);  }
                                cell20.setCellValue(listSetmeal.get(z).getAttention());//套餐注意事项

                                //根据套餐id获取检查组信息
                                System.out.println(listSetmeal.get(z).getId());
                                List<CheckGroup> listCheckGroup = memberService.findCheckGroupBySetmealId(listSetmeal.get(z).getId());
                                System.out.println(Arrays.toString(listCheckGroup.toArray()));
                                for (int x = 0; x < listCheckGroup.size(); x++) {
                                    XSSFRow row21 = sheet.getRow(22);
                                    XSSFCell cell21 = row21.getCell(j+7);
                                    if(cell21 == null){  cell21 = row21.createCell(j+7);  }
                                    cell21.setCellValue(listCheckGroup.get(x).getName());//检查组名称

                                    XSSFRow row22 = sheet.getRow(23);
                                    XSSFCell cell22 = row22.getCell(j+7);
                                    if(cell22 == null){  cell22 = row22.createCell(j+7);  }
                                    cell22.setCellValue(listCheckGroup.get(x).getCode());//检查组编码

                                    XSSFRow row23 = sheet.getRow(24);
                                    XSSFCell cell23 = row23.getCell(j+7);
                                    if(cell23 == null){  cell23 = row23.createCell(j+7);  }
                                    cell23.setCellValue(listCheckGroup.get(x).getHelpCode());//检查组助记码

                                    XSSFRow row24 = sheet.getRow(25);
                                    XSSFCell cell24 = row24.getCell(j+7);
                                    if(cell24 == null){  cell24 = row24.createCell(j+7);  }
                                    cell24.setCellValue(listCheckGroup.get(x).getSex());//检查组适用性别


                                    XSSFRow row25 = sheet.getRow(26);
                                    XSSFCell cell25 = row25.getCell(j+7);
                                    if(cell25 == null){  cell25 = row25.createCell(j+7);  }
                                    cell25.setCellValue(listCheckGroup.get(x).getRemark());//检查组说明

                                    XSSFRow row26 = sheet.getRow(27);
                                    XSSFCell cell26 = row26.getCell(j+7);
                                    if(cell26 == null){  cell26 = row26.createCell(j+7);  }
                                    cell26.setCellValue(listCheckGroup.get(x).getAttention());//检查组注意事项

                                    //根据检查组id获取检查项
                                    List<CheckItem> listCheckItem = memberService.findCheckItemByCheckGroupId(listCheckGroup.get(x).getId());
                                    System.out.println(Arrays.toString(listCheckItem.toArray()));
                                    for (int s = 0; s < listCheckItem.size(); s++) {
                                        XSSFRow row27 = sheet.getRow(28);
                                        XSSFCell cell27 = row27.getCell(j+7);
                                        if(cell27 == null){  cell27 = row27.createCell(j+7);  }
                                        cell27.setCellValue(listCheckItem.get(s).getName());//项目名称

                                        XSSFRow row28 = sheet.getRow(29);
                                        XSSFCell cell28 = row28.getCell(j+7);
                                        if(cell28 == null){  cell28 = row28.createCell(j+7);  }
                                        cell28.setCellValue(listCheckItem.get(s).getCode());//项目编码


                                        XSSFRow row29 = sheet.getRow(30);
                                        XSSFCell cell29 = row29.getCell(j+7);
                                        if(cell29 == null){  cell29 = row29.createCell(j+7);  }
                                        cell29.setCellValue(listCheckItem.get(s).getSex());//项目适用性别


                                        XSSFRow row30 = sheet.getRow(31);
                                        XSSFCell cell30 = row30.getCell(j+7);
                                        if(cell30 == null){  cell30 = row30.createCell(j+7);  }
                                        cell30.setCellValue(listCheckItem.get(s).getAge());//项目适用年龄


                                        XSSFRow row31 = sheet.getRow(32);
                                        XSSFCell cell31 = row31.getCell(j+7);
                                        if(cell31 == null){  cell31 = row31.createCell(j+7);  }
                                        cell31.setCellValue(listCheckItem.get(s).getType());//项目类型


                                        XSSFRow row32 = sheet.getRow(33);
                                        XSSFCell cell32 = row32.getCell(j+7);
                                        if(cell32 == null){  cell32 = row32.createCell(j+7);  }
                                        cell32.setCellValue(listCheckItem.get(s).getPrice());//项目价格

                                        XSSFRow row33 = sheet.getRow(34);
                                        XSSFCell cell33 = row33.getCell(j+7);
                                        if(cell33 == null){  cell33 = row33.createCell(j+7);  }
                                        cell33.setCellValue(listCheckItem.get(s).getRemark());//项目说明

                                        XSSFRow row34 = sheet.getRow(35);
                                        XSSFCell cell34 = row34.getCell(j+7);
                                        if(cell34 == null){  cell34 = row34.createCell(j+7);  }
                                        cell34.setCellValue(listCheckItem.get(s).getAttention());//项目注意事项

                                    }
                                }
                            }

                        }



                }

            }


            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();
            //这里设置的文件格式是application/x-excel
            response.setContentType("application/x-excel");
            response.setHeader("Content-Disposition", "attachment;filename=report.xlsx");
//            ServletOutputStream outputStream = response.getOutputStream();
//            memberService.exportExcel(memberList, outputStream);
//            System.out.println(1);
//            if (outputStream != null) {
//                outputStream.close();
//            }
            //5.输出
            excel.write(out);
            out.flush();
            excel.close();
            out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @RequestMapping("/findByIds")
    public Integer[] findByIds(){
        Integer[] ids = memberService.findByIds();
        return ids;
    }

}
