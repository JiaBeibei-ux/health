package com.haowaner.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.haowaner.dao.CheckGroupDao;
import com.haowaner.entity.PageResult;
import com.haowaner.pojo.CheckGroup;
import com.haowaner.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查组服务实现类
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService{
    @Autowired
    private CheckGroupDao checkGroupDao;

    //分页查询
    @Override
    public PageResult findCondition(Integer currentPage, String queryString, Integer pageSize) {
        //分页插件的使用
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> checkGroups = checkGroupDao.findCondition(queryString);
        return new PageResult(checkGroups.getTotal(),checkGroups.getResult());
    }

    //添加
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        checkGroupDao.add(checkGroup);
        //一对多的关系处理一下
        setCheckItemAndCheckGroup(checkGroup,checkitemIds);
    }

    //编辑
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //通过checkItemIds删除中间表 在中间表中重新添加
        for (Integer checkitemId : checkitemIds) {
            checkGroupDao.deleteByCheckitemId(checkitemId);
        }
        setCheckItemAndCheckGroup(checkGroup,checkitemIds);
    }
    //监察组合检查项的关系
    /*private void editCheckItemAndCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
        Integer id = checkGroup.getId();
        List<Integer> checkBeforeIds = checkGroupDao.findByCheckId(id);
        if(checkBeforeIds.size()!=0 && checkBeforeIds!=null){
         for (Integer checkitemId : checkitemIds) {
            for (Integer checkBeforeId : checkBeforeIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkitem_id", checkitemId);
                //先插入检查项 然后获得检查组id
                map.put("checkgroup_id", checkGroup.getId());
                map.put("checkBeforeId", checkBeforeId);
                //保存到数据库中
                checkGroupDao.editCheckItemAndCheckGroup(map);
            }
          }
         }else{
            //checkBeforeIds为空说明 检查组和检查项之前没有做关联 重新再中间表插入一下
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map = new HashMap<String,Integer>();
                map.put("checkitem_id", checkitemId);
                //先插入检查项 然后获得检查组id
                map.put("checkgroup_id", checkGroup.getId());
                checkGroupDao.insertCheckItemAndCheckGroup();
            }
    }*/

    //通过id查询 回显数据
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    //删除方法
    @Override
    public void delete(Integer id) {
        //删除检查组和检查项的中间表
        checkGroupDao.deleteByGid(id);
        //删除检查组和会员套餐的中间表
        checkGroupDao.deleteByGid2(id);
        checkGroupDao.delete(id);
    }

    //通过检查组id 查询检查项id
    @Override
    public List<Integer> findByCheckId(Integer id) {
        return checkGroupDao.findByCheckId(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    //将检查组和检查项做个关联处理 在保存一下
    private void setCheckItemAndCheckGroup(CheckGroup checkGroup, Integer[] checkitemIds) {
          //先遍历checkitemIds  然后用map存一下
        for (Integer checkitemId : checkitemIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("checkitem_id",checkitemId);
            //先插入检查项 然后获得检查组id
            map.put("checkgroup_id",checkGroup.getId());
            //保存到数据库中
            checkGroupDao.addCheckItemAndCheckGroup(map);
        }
    }
}
