package com.haowaner.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.haowaner.constant.RedisConstant;
import com.haowaner.dao.SetmealDao;
import com.haowaner.entity.PageResult;
import com.haowaner.pojo.CheckGroup;
import com.haowaner.pojo.Setmeal;
import com.haowaner.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 会员套餐业务接口实现类
 */
@Service(interfaceClass= SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    private SetmealDao setmealDao;
    @Autowired
    private JedisPool jedisPool;

    //添加方法
    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
       setmealDao.add(setmeal);
       //将保存到数据库的图片放到redis中
        Jedis jedis = new Jedis();
        jedisPool.getResource().sadd(RedisConstant.SET_DB_PIC_CONSTANT,setmeal.getImg());
       //处理一下 会员套餐和检查组的关系
       addSetmealAndCheckgroups(setmeal,checkgroupIds);
    }

    /**
     * 展示数据
     * @param currentPage 当前页
     * @param pageSize  一页几行 分页插件使用的数据
     * @param queryString 模糊查询条件
     * @return
     */
    @Override
    public PageResult list(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmeals = setmealDao.list(queryString);
        return new PageResult(setmeals.getTotal(),setmeals.getResult());
    }

    @Override
    public void delete(Integer id) {
        //先删除中间表
        setmealDao.deleteSetmealAndCheckGroup(id);
        setmealDao.delete(id);
    }

    //回显套餐数据需要 通过id查询
    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    //回显套餐下的检查组的信息返回的IDS
    @Override
    public List<Integer> findByCheckGroupId(Integer id) {
        return setmealDao.findByCheckGroupId(id);
    }

    //编辑时删除中间表
    @Override
    public void edit(Setmeal setmeal, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            setmealDao.deleteSetmealAndCheckGroup2(checkgroupId);
        }
        //重新插入到中间表中 复用方法
        addSetmealAndCheckgroups(setmeal,checkgroupIds);
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    //通过id查询套餐  使用SQL字句查询检查组和检查项
    @Override
    public Setmeal findById2(Integer id) {
        return setmealDao.findById2(id);
    }

    //查询出所有的套餐
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }

    /**
     * 会员套餐和检查组的关系
     * @param setmeal
     * @param checkgroupIds
     */
    private void addSetmealAndCheckgroups(Setmeal setmeal, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("setmeal_id",setmeal.getId());
            map.put("checkgroup_id",checkgroupId);
            setmealDao.addSetmealAndCheckgroups(map);
        }
    }

}
