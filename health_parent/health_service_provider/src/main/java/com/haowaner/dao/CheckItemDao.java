package com.haowaner.dao;

import com.github.pagehelper.Page;
import com.haowaner.entity.PageResult;
import com.haowaner.entity.Result;
import com.haowaner.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 检查项持久层
 */
@Repository
public interface CheckItemDao {
    //检查项添加
    public void add(CheckItem checkItem);

    //查询总记录数
    public long findTotal();

    //有条件就模糊查询  没有就普通查询
    public Page<CheckItem> findByCondition(String queryString);

    //通过id删除
    public void delete(Integer id);

    //通过id查询
    public CheckItem findById(Integer id);

    //编辑
    public void edit(CheckItem checkItem);

    //查询所有
    public List<CheckItem> findAll();

    //当检查项和检查组关联时，先删除中间表
    public void deleteByCheckItemId(Integer id);

    //检查组关联查询 根据检查项查询
    //public List<CheckItem> findByCheckId(Long id);
}
