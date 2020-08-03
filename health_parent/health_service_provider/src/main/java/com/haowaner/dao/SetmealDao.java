package com.haowaner.dao;

import com.github.pagehelper.Page;
import com.haowaner.pojo.CheckGroup;
import com.haowaner.pojo.Setmeal;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 会员套餐持久层
 */
@Repository
public interface SetmealDao {

    //添加会员套餐方法
    public void add(Setmeal setmeal);

    //添加会员套餐和检查组
    public void addSetmealAndCheckgroups(Map<String, Integer> map);

    //查询所有加模糊
    public Page<Setmeal> list(String queryString);

    //通过id查询 回显数据需要
    public Setmeal findById(Integer id);

    //回显检查组的信息
    public List<Integer> findByCheckGroupId(Integer id);

    //删除中间表
    public void deleteSetmealAndCheckGroup(Integer id);
    //删除方法
    public void delete(Integer id);

    //编辑数据时 先删中间表
    public void deleteSetmealAndCheckGroup2(Integer checkgroupId);

    //查询所有
    public List<Setmeal> findAll();

    //通过id查询套餐  使用SQL字句查询检查组和检查项
    public Setmeal findById2(Integer id);

    //查询所有的套餐
    public List<Map<String, Object>> findSetmealCount();
}
