package com.haowaner.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.haowaner.constant.MessageConstant;
import com.haowaner.entity.PageResult;
import com.haowaner.entity.QueryPageBean;
import com.haowaner.entity.Result;
import com.haowaner.pojo.CheckItem;
import com.haowaner.service.CheckItemService;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查项控制器
 */
@RestController //整合注解 responseBody和controller
@RequestMapping("/check")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 模糊查询  还没有完善
     * @param queryPageBean
     * @return
     */
    //权限校验
    @PreAuthorize("hasAnyAuthority('CHECKITEM_QUERY')")
    @RequestMapping("/list")
    public Result list(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = checkItemService.findByCondition(queryPageBean.getCurrentPage(), queryPageBean.getQueryString(), queryPageBean.getPageSize());
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
    /**
     * 添加方法
     * @param checkItem
     * @return
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_ADD')")
    @RequestMapping("/add")
    //@RequestBody将前台传来的json转化为数据
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 查询所有  检查组关联查询时用的
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        List<CheckItem> checkItems = checkItemService.findAll();
        if(checkItems!=null && checkItems.size()>0){
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItems);
        }
        return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
    }

    /**
     * 删除方法
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_DELETE')")
    @RequestMapping("/delete")
    public Result delete(@RequestParam(value = "id",required = true) Integer id){
        try {
            checkItemService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 通过id查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(@RequestParam(value="id",required =true) Integer id){
        CheckItem checkItem;
        try {
            checkItem = checkItemService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }
    /**
     * 编辑
     * @param checkItem
     * @return
     */
    @PreAuthorize("hasAnyAuthority('CHECKITEM_EDIT')")
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
