package com.haowaner.dao;

import com.haowaner.pojo.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 会员持久层接口
 */
@Repository
public interface MemberDao {
    //通过手机号判断用户是否为会员
    public Member findByTel(String telephone);

    //注册会员
    public void add(Member member);

    //根据月份去查询会员人数
    public Integer findMemberCountByMonth(String month);

    //查询今日新增
    public Integer findMemberCountByDate(String today);

    //总会员数
    public Integer findMemberTotalCount();

    //本周新增会员数
    public Integer findMemberCountAfterDate(String thisWeekMonday);
}
