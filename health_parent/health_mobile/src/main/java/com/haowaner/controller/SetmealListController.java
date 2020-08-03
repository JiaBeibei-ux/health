package com.haowaner.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.haowaner.constant.MessageConstant;
import com.haowaner.entity.Result;
import com.haowaner.pojo.Setmeal;
import com.haowaner.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 套餐列表控制器
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealListController {

    @Reference
    private SetmealService setmealService;

    /**
     * 获取所有数据
     * @return
     */
    @RequestMapping("/getSetmeal")
    public Result findAll(){
        try {
            List<Setmeal> setmeals = setmealService.findAll();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeals);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 通过id查询套餐  使用SQL字句查询检查组和检查项
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(@RequestParam(value="id") Integer id){
        try {
            Setmeal setmeal = setmealService.findById2(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
