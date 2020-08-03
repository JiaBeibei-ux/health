package com.haowaner.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.haowaner.dao.CheckItemDao;
import com.haowaner.entity.PageResult;
import com.haowaner.pojo.CheckItem;
import com.haowaner.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    @Override
    public long findTotal() {
        return checkItemDao.findTotal();
    }

    @Override
    public PageResult findByCondition(Integer currentPage, String queryString, Integer pageSize) {
        //分页插件
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> checkItems = checkItemDao.findByCondition(queryString);
        return new PageResult(checkItems.getTotal(),checkItems.getResult());
    }

    @Override
    public void delete(Integer id) {
        //当检查项和检查组关联时，先删除中间表
        checkItemDao.deleteByCheckItemId(id);
        checkItemDao.delete(id);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
