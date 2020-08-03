package com.haowaner.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.haowaner.constant.MessageConstant;
import com.haowaner.entity.PageResult;
import com.haowaner.entity.QueryPageBean;
import com.haowaner.entity.Result;
import com.haowaner.pojo.CheckGroup;
import com.haowaner.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 检查组控制器
 */
@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加方法
     *
     * @param checkGroup
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        //先传两个参数  到业务层在处理
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 展示数据  模糊查询有点问题
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/list")
    public Result list(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = checkGroupService.findCondition(queryPageBean.getCurrentPage(), queryPageBean.getQueryString(), queryPageBean.getPageSize());
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, pageResult);
    }

    /**
     * 编辑数据时回显数据  但有点问题
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(@RequestParam(value = "id", required = true) Integer id) {
        CheckGroup checkGroup;
        try {
            checkGroup = checkGroupService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
    }

    /**
     * 编辑时回显检查组所属检查项id
     *
     * @param id
     * @return
     */
    @RequestMapping("/findByCheckId")
    public Result findByCheckId(@RequestParam(value = "id", required = true) Integer id) {
        List<Integer> checkIds;

        try {
            checkIds = checkGroupService.findByCheckId(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkIds);
    }

    /**
     * 编辑  还没有写好
     *
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.edit(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 会员套餐需要 查询所有
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> checkGroups;
        try {
            checkGroups = checkGroupService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroups);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(@RequestParam(value = "id", required = true) Integer id) {
        try {
            //先删除中间表
            //checkGroupService.deleteByGid(id);
            checkGroupService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            //因为外键有约束 会有删除不了的情况
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
