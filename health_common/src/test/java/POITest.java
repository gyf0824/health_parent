import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;

public class POITest {
    //使用poi读取Excel文件
    //@Test
    public void test01() throws IOException {
        //加载指定文件，创建一个Excel对象（就是工作簿）
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("G:\\学习\\传智健康-加密\\视频\\day05\\czzbk.xlsx")));
        //获取工作表，既可以按照工作表的顺序，也可以根据工作表的名称
        XSSFSheet sheets = excel.getSheetAt(0);
        //遍历工作表，获取行对象
        //row行接口
        for (Row row : sheets) {
            //遍历行对象，获取单元格对象
            for (Cell cell : row) {
                //获取每个单元格的值
                String stringCellValue = cell.getStringCellValue();
                System.out.println(stringCellValue);
            }
        }
        excel.close();
    }

    //使用POI读取Excel文件中的数据
    //@Test
    public void test2() throws Exception{
        //加载指定文件，创建一个Excel对象（工作簿）
        XSSFWorkbook excel = new XSSFWorkbook(new FileInputStream(new File("G:\\学习\\传智健康-加密\\视频\\day05\\czzbk.xlsx")));
        //读取Excel文件中第一个Sheet标签页
        XSSFSheet sheet = excel.getSheetAt(0);
        //获得当前工作表中最后一个行号，需要注意：行号从0开始
        int lastRowNum = sheet.getLastRowNum();
        System.out.println("lastRowNum = " + lastRowNum);
        System.out.println("- - - - - - - -");
        for(int i=0;i<=lastRowNum;i++){
            //row行接口的实现类
            XSSFRow row = sheet.getRow(i);//根据行号获取每一行
            //获得当前行最后一个单元格索引
            short lastCellNum = row.getLastCellNum();
            System.out.println("lastCellNum = " + lastCellNum);
            for(int j=0;j<lastCellNum;j++){
                XSSFCell cell = row.getCell(j);//根据单元格索引获得单元格对象
                System.out.println(cell.getStringCellValue());
            }
        }
        //关闭资源
        excel.close();
    }

    //使用POI向Excel文件写入数据，并且通过输出流将创建的Excel文件保存到本地磁盘
    //@Test
    public void test3() throws Exception{
        //在内存中创建一个Excel文件（工作簿）
        XSSFWorkbook excel = new XSSFWorkbook();
        //创建一个工作表对象
        XSSFSheet sheet = excel.createSheet("传智播客");
        //在工作表中创建行对象
        XSSFRow title = sheet.createRow(0);
        //在行中创建单元格对象
        title.createCell(0).setCellValue("姓名");
        title.createCell(1).setCellValue("地址");
        title.createCell(2).setCellValue("年龄");

        XSSFRow dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue("小明");
        dataRow.createCell(1).setCellValue("北京");
        dataRow.createCell(2).setCellValue("20");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小王");
        row2.createCell(2).setCellValue("20");

        //创建一个输出流，通过输出流将内存中的Excel文件写到磁盘
        FileOutputStream out = new FileOutputStream(new File("G:\\学习\\传智健康-加密\\视频\\day05\\gyf.xlsx"));
        //写入
        excel.write(out);
        //刷新参数
        out.flush();
        excel.close();
        out.close();
    }
}
