package com.haowaner.dao;

import com.github.pagehelper.Page;
import com.haowaner.pojo.CheckGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 检查组持久层
 */
@Repository
public interface CheckGroupDao {
    //检查组添加
    public void add(CheckGroup checkGroup);

    //把关联关系 检查组和检查项的关联关系存起来
    public void addCheckItemAndCheckGroup(Map<String, Integer> map);

    //分页查询
    public Page<CheckGroup> findCondition(String queryString);

   /* //编辑 有点多余了
    public void edit(CheckGroup checkGroup);*/

    //将检查组和检查项编辑一下
    public void editCheckItemAndCheckGroup(Map<String, Integer> map);

    //通过id查询
    public CheckGroup findById(Integer id);

    //先删除中间表
    public void deleteByGid(Integer id);
    public void deleteByGid2(Integer id);

    //删除方法
    public void delete(Integer id);

    //通过id查询检查项
    public List<Integer> findByCheckId(Integer id);

    //会员套餐需要的 查询所有
    public List<CheckGroup> findAll();

    //编辑先删除中间表在中间表重新添加
    public void deleteByCheckitemId(Integer checkitemId);
}
