package com.itheima.utils;


import java.util.List;

import javax.servlet.ServletOutputStream;

import com.itheima.pojo.Member;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;


/**
 * excel工具类,支持批量导出
 * @author lizewu
 *
 */
public class ExcelUtil {

    /**
     * 将会员的信息导入到excel文件中去
     * @param memberList 会员列表
     * @param out 输出表
     */
    public static void exportMemberExcel(List<Member> memberList, ServletOutputStream out)
    {
        try{
            //1.创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            //1.1创建合并单元格对象
            CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,13);//起始行,结束行,起始列,结束列
            //1.2头标题样式
            HSSFCellStyle headStyle = createCellStyle(workbook,(short)16);
            //1.3列标题样式
            HSSFCellStyle colStyle = createCellStyle(workbook,(short)13);
            //2.创建工作表
            HSSFSheet sheet = workbook.createSheet("会员信息汇总");
            //2.1加载合并单元格对象
            sheet.addMergedRegion(callRangeAddress);
            //设置默认列宽
            sheet.setDefaultColumnWidth(25);
            //3.创建行
            //3.1创建头标题行;并且设置头标题
            HSSFRow row = sheet.createRow(0);
            HSSFCell cell = row.createCell(0);

            //加载单元格样式
            cell.setCellStyle(headStyle);
            cell.setCellValue("会员信息汇总");

            //3.2创建列标题;并且设置列标题
            HSSFRow row2 = sheet.createRow(1);
            String[] titles = {"档案号","姓名","性别","身份证","手机号","注册时间","邮箱","出生日期","备注"};
            for(int i=0;i<titles.length;i++)
            {
                HSSFCell cell2 = row2.createCell(i);
                //加载单元格样式
                cell2.setCellStyle(colStyle);
                cell2.setCellValue(titles[i]);
            }


            //4.操作单元格;将用户列表写入excel
            if(memberList != null)
            {
                for(int j=0;j<memberList.size();j++)
                {
                    //创建数据行,前面有两行,头标题行和列标题行
                    HSSFRow row3 = sheet.createRow(j+3);
                    HSSFCell cell1 = row3.createCell(0);
                    cell1.setCellValue(memberList.get(j).getFileNumber());//档案号
                    HSSFCell cell2 = row3.createCell(1);
                    cell2.setCellValue(memberList.get(j).getName());//姓名
                    HSSFCell cell3 = row3.createCell(2);
                    cell3.setCellValue(memberList.get(j).getSex());//性别
                    HSSFCell cell4 = row3.createCell(3);
                    cell4.setCellValue(memberList.get(j).getIdCard());//身份证
                    HSSFCell cell5 = row3.createCell(4);
                    cell5.setCellValue(memberList.get(j).getPhoneNumber());//手机号
                    HSSFCell cell6 = row3.createCell(5);
                    cell6.setCellValue(memberList.get(j).getRegTime());//注册时间
                    HSSFCell cell7 = row3.createCell(6);
                    cell7.setCellValue(memberList.get(j).getEmail());//邮箱
                    HSSFCell cell8 = row3.createCell(7);
                    cell8.setCellValue(memberList.get(j).getBirthday());//出生日期
                    HSSFCell cell9 = row3.createCell(8);
                    cell9.setCellValue(memberList.get(j).getRemark());//备注

                }
            }
            //5.输出
            workbook.write(out);
            workbook.close();
            //out.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param workbook
     * @param fontsize
     * @return 单元格样式
     */
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize) {
        // TODO Auto-generated method stub
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
        //创建字体
        HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setFontHeightInPoints(fontsize);
        //加载字体
        style.setFont(font);
        return style;
    }
}