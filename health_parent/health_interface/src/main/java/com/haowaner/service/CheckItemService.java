package com.haowaner.service;

import com.haowaner.entity.PageResult;
import com.haowaner.pojo.CheckItem;

import java.util.List;

/**
 * 检查业务层接口
 */
public interface CheckItemService {
    //检查项添加
    public void add(CheckItem checkItem);

    //查询总记录数
    public long findTotal();

    //有条件就模糊查询  没有就普通查询
    public PageResult findByCondition(Integer currentPage, String queryString, Integer pageSize);

    //删除
    public void delete(Integer id);

    //通过id查询
    public CheckItem findById(Integer id);

    //编辑
    public void edit(CheckItem checkItem);

    //查询所有
    public List<CheckItem> findAll();
}
