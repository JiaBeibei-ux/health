package com.haowaner;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;

//POI练习 读写
public class SysTest {
    @Test
    public void test() throws Exception {
        //读取文档
        XSSFWorkbook excel = new XSSFWorkbook("D:\\1821\\aaa.xlsx");
        //获取工作表
        XSSFSheet sheetAt = excel.getSheetAt(0);
        for (Row row : sheetAt) {
            for (Cell cell : row) {
                String stringCellValue = cell.getStringCellValue();
                System.out.println(stringCellValue);
            }
        }
    }
    @Test
    public void test2() throws Exception{
        //获取表单
        XSSFWorkbook sheets = new XSSFWorkbook("D:\\1821\\aaa.xlsx");
        //遍历表格元素
        //获得第一张表
        XSSFSheet sheetAt = sheets.getSheetAt(0);
        int lastRowNum = sheetAt.getLastRowNum();
        //获取最后一行的行号
        System.out.println("lRNum  "+lastRowNum);
        for(int i=0;i<=lastRowNum;i++){
            XSSFRow row = sheetAt.getRow(i);
            short lastCellNum = row.getLastCellNum();
            System.out.println(lastCellNum);
            //获取每一个单元格
            for (int j=0;j<lastCellNum;j++){
                String stringCellValue = row.getCell(j).getStringCellValue();
                System.out.println(stringCellValue);
            }
        }
    }
    @Test
    public void test3() throws Exception{
        //创建一个工作簿
        XSSFWorkbook workbook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet haowaner = workbook.createSheet("haowaner");
        //创建行
        XSSFRow row = haowaner.createRow(0);
        //创建单元格
        row.createCell(0).setCellValue("标号");
        row.createCell(1).setCellValue("年龄");
        row.createCell(2).setCellValue("名称");
        //创建行
        XSSFRow row1 = haowaner.createRow(1);
        //创建单元格
        row1.createCell(0).setCellValue("011");
        row1.createCell(1).setCellValue("10");
        row1.createCell(2).setCellValue("名称");
        //保存磁盘中
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\1821\\bb.xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        workbook.close();
    }
}
