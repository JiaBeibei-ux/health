package com.haowaner.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.haowaner.constant.MessageConstant;
import com.haowaner.entity.Result;
import com.haowaner.service.MemberService;
import com.haowaner.service.ReportService;
import com.haowaner.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表生成控制器
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;
    /**
     * 根据月份查询会员人数
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//获得当前日期之前12个月的日期
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
            //吧之前12个月放到list里面 只放到月份
            calendar.add(calendar.MONTH,1);
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
        Map<String,Object> map = new HashMap<>();
        map.put("months",list);
        //根据月份去查找会员人数
        List<Integer> memberCount = memberService.findMemberCountByMonth(list);
        map.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    /**
     * 套餐占比统计
     * 前端接受数据
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
        //查出所有套餐数量 和套餐名
        List<Map<String,Object>> list = setmealService.findSetmealCount();
        Map<String,Object> map = new HashMap<>();
        map.put("setmealCount",list);
        //套餐名
        List<String> setmealNames = new ArrayList<>();
        //将查询出的套餐遍历一下
        for (Map<String, Object> m : list) {
            String name = (String)m.get("name");
            setmealNames.add(name);
        }
        map.put("setmealNames",setmealNames);
        return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    /**
     * 获取运营数据
     * 没有什么方法 都在业务实现层
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String,Object> result = reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出excel报表 往报表中填数据
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            //远程调用报表服务获取报表数据
            Map<String,Object> result = reportService.getBusinessReport();
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
            //获取excel模板文件的绝对路径 File.separator是系统默认的文件分割符号，屏蔽了这些系统的区别
            System.out.println(request.getSession().getServletContext().getRealPath("/"));
            String templateRealPath = request.getSession().getServletContext().getRealPath("static\\template")+ File.separator+"report_template.xlsx";
            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(templateRealPath)));
            //第一张表
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取第三行
            XSSFRow row = sheet.getRow(2);
            //给第三行第六个单元格 日期赋值
            row.getCell(5).setCellValue(reportDate);
            row = sheet.getRow(4);
            //给新增会员数（本日）
            row.getCell(5).setCellValue(todayNewMember);
            row.getCell(7).setCellValue(totalMember);//会员总数

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
            //热门套餐遍历一下 放到表格中
            int rowNum = 12;
            for (Map map : hotSetmeal) {
                String name = (String)map.get("name");
                Long setmeal_count = (Long)map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal)map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }
            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            //响应输出格式
            response.setContentType("application/vnd.ms-excel");
            //下载提示文件名
            response.setHeader("content‐Disposition","attachment;filename=report.xlsx");
            //将内容写到工作簿中
            workbook.write(out);
            out.flush();
            out.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL,null);
        }
    }
}
