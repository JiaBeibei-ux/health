package com.haowaner.service;

import com.haowaner.entity.PageResult;
import com.haowaner.pojo.CheckGroup;

import java.util.List;

/**
 * 检查组服务
 */
public interface CheckGroupService {

    //分页查询
    public PageResult findCondition(Integer currentPage, String queryString, Integer pageSize);

    //添加
    public void add(CheckGroup checkGroup, Integer[] checkitemIds);

    //编辑
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds);

    //通过id查询
    public CheckGroup findById(Integer id);

    //删除方法
    public void delete(Integer id);

    //通过id查询检查项
    public List<Integer> findByCheckId(Integer id);

    //会员套餐需要的 查询所有
    public List<CheckGroup> findAll();
}
