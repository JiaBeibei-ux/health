package com.haowaner.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.haowaner.constant.MessageConstant;
import com.haowaner.entity.Result;
import com.haowaner.pojo.OrderSetting;
import com.haowaner.service.OrderSettingService;
import com.haowaner.utils.POIUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预约人数设置控制器
 */
@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {
    @Reference
    private OrderSettingService orderSettingService;
    //将上传的xlsx文件转换一下  使用POI API
    @RequestMapping("/upload")
    public Result xlsxUpload(@RequestParam(value="excelFile") MultipartFile excelFile) throws Exception{
        //使用工具类 读入excel文件，解析后返回 excel文件所有的内容 有效信息
        List<String[]> list = POIUtils.readExcel(excelFile);
        //用来存放ordersetting相关信息
        List<OrderSetting> orderSettings = new ArrayList<>();
        for (String[] strings : list) {
            OrderSetting orderSetting = new OrderSetting(new Date(strings[0]),Integer.parseInt(strings[1]));
            orderSettings.add(orderSetting);
        }
        orderSettingService.add(orderSettings);
        return new Result(true, MessageConstant.UPLOAD_SUCCESS);
    }

    /**
     * 根据月份去查询当月的预约信息
     * @param date
     * @return
     */
    @RequestMapping("/getNumberByMonth")
    public Result findNumberByMonth(@RequestParam(value="date") String date){
        try {
            List<Map> numberByMonth = orderSettingService.findNumberByMonth(date);
            return new Result(true,"查询成功",numberByMonth);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"查询失败！");
        }
    }

    /**
     * 根据具体的日期取修改预约人数
     * @param orderSetting
     * @return
     */
    @RequestMapping("/updateNumberByDate")
    public Result updateNumberByDate(@RequestBody OrderSetting orderSetting){
        try {
            orderSettingService.editByDate(orderSetting);
            return new Result(true, "编辑预约人数成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, "编辑预约人数失败！");
        }
    }
}
