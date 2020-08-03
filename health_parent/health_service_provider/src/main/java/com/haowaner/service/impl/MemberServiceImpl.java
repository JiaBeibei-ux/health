package com.haowaner.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.haowaner.dao.MemberDao;
import com.haowaner.pojo.Member;
import com.haowaner.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员服务实现类
 */
@Service(interfaceClass=MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTel(String tel) {
        return memberDao.findByTel(tel);
    }

    @Override
    public void add(Member member) {
       memberDao.add(member);
    }

    //根据月份去查询
    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
        List<Integer> list = new ArrayList();
        //先遍历一下月份moths
        for (String month : months) {
            //注意一下格式 每个月末统计一下会员人数
            month = month+".31";
            Integer memberCount = memberDao.findMemberCountByMonth(month);
            list.add(memberCount);
        }
        return list;
    }
}
