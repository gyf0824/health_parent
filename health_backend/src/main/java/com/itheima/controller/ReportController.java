package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.MemberService_gyf;
import com.itheima.ReportService;
import com.itheima.SetmealService;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService_gyf memberService;
    @Reference
    private SetmealService setmealService;
    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        //使用模拟数据测试对象格式是否能够转化成echarts所需要的格式
        Map<String,Object> map = new HashMap<>();
        List<String> months = new ArrayList<>();
      /*  months.add("2019.01");
        months.add("2019.02");
        months.add("2019.03");
        months.add("2019.04");
        months.add("2019.05");*/

        Calendar calendar = Calendar.getInstance();//获得日历对象，模拟时间就是当前时间
        //计算过去一年的12个月
        calendar.add(Calendar.MONTH,-13);//获得当前时间往前推13个月的时间
        for(int i=0;i<12;i++){
            calendar.add(Calendar.MONTH,1);//获得当前时间往后推一个月日期
            Date date = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(date));
        }
        map.put("months",months);
        List<Integer> memberCount = new ArrayList<>();
  /*      memberCount.add(200);
        memberCount.add(300);
        memberCount.add(400);
        memberCount.add(500);
        memberCount.add(200);*/
        List<Integer> count = memberService.findMemberCountByMonths(months);
        map.put("memberCount",count);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    @RequestMapping("getSetmealReport")
    private Result getSetmealReport(){
        //使用模拟数据测试对象格式是否能够转化成echarts所需要的格式
        Map<String,Object> map = new HashMap<>();
/*        List<String> setmealNames = new ArrayList<>();
        setmealNames.add("入职体检套餐");
        setmealNames.add("孕前检查套餐");
        map.put("setmealNames",setmealNames);


        List<Map<String,Object>> setmealCount = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","入职体检套餐");
        map1.put("value",150);
        setmealCount.add(map1);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","孕前检查套餐");
        map2.put("value",100);
        setmealCount.add(map2);
        map.put("setmealCount",setmealCount);*/
        try {
            List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
            map.put("setmealCount",setmealCount);

            List<String> setmealNames = new ArrayList<>();
            for (Map<String, Object> stringObjectMap : setmealCount) {
                String name = (String) stringObjectMap.get("name");
                setmealNames.add(name);
            }
            map.put("setemalName",setmealNames);

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    //运营数据统计
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        //使用模拟数据测试对象格式是否能够转化成echarts所需要的格式
/*        Map<String,Object> map = new HashMap<>();
        map.put("reportData","2019.12.5");
        map.put("todayNewMember",100);
        map.put("totalMember",30000);
        map.put("thisWeekNewMember",400);
        map.put("thisMonthNewMember",1600);
        map.put("todayOrderNumber",500);
        map.put("todayVisitsNumber",100);
        map.put("thisWeekOrderNumber",700);
        map.put("thisWeekVisitsNumber",250);
        map.put("thisMonthOrderNumber",1500);
        map.put("thisMonthVisitsNumber",750);
        List<Map<String,Object>> hotSetmeal = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐");
        map1.put("setmeal_count",350);
        map1.put("proportion",0.35);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","阳光爸妈升级肿瘤12项筛查体检套餐");
        map2.put("setmeal_count",300);
        map2.put("proportion",0.30);
        hotSetmeal.add(map1);
        hotSetmeal.add(map2);
        map.put("hotSetmeal",hotSetmeal);*/

        try {
            Map<String,Object> map = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    //导出运营数据
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try{
            Map<String,Object> result = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            String filePath = request.getSession().getServletContext().getRealPath("template")+ File.separator+"report_template.xlsx";
            //基于提供的Excel模板文件在内存中创建一个Excel表格对象
            XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //读取第一个工作表
            XSSFSheet sheet = excel.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            System.out.println(row);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNum = 12;
            for(Map map : hotSetmeal){//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum ++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //使用输出流进行表格下载,基于浏览器作为客户端下载
            OutputStream out = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            excel.write(out);

            out.flush();
            out.close();
            excel.close();
            return null;
        }catch (Exception e){
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

}
