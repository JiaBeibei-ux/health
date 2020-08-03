package com.haowaner.service;

import com.haowaner.entity.PageResult;
import com.haowaner.pojo.CheckGroup;
import com.haowaner.pojo.Setmeal;

import java.util.List;
import java.util.Map;

/**
 * 会员套餐服务接口
 */
public interface SetmealService {
    //添加会员套餐方法
    public void add(Setmeal setmeal, Integer[] checkgroupIds);

    //分页及模糊查询
    public PageResult list(Integer currentPage, Integer pageSize, String queryString);

    //删除方法
    public void delete(Integer id);

    //通过id查询 回显数据需要
    public Setmeal findById(Integer id);

    //回显检查组的信息
    public List<Integer> findByCheckGroupId(Integer id);

    //编辑时删除中间表
    public void edit(Setmeal setmeal, Integer[] checkgroupIds);

    //查询所有
    public List<Setmeal> findAll();

    //通过id查询套餐  使用SQL字句查询检查组和检查项
    public Setmeal findById2(Integer id);

    //统计套餐占比
    public List<Map<String, Object>> findSetmealCount();

    //编辑时删除中间表
    //public void deleteSetmealAndCheckgroupById(Integer[] checkgroupIds);

}
