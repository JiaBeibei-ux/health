package com.haowaner.service;

import com.haowaner.pojo.Member;

import java.util.List;

/**
 * 成员服务接口
 */
public interface MemberService {

    //根据电话号码查询
    public Member findByTel(String tel);

    //添加
    public void add(Member member);

    //根据月份查找人数
    public List<Integer> findMemberCountByMonth(List<String> month);
}
