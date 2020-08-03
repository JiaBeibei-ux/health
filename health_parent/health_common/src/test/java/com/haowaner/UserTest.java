package com.haowaner;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UserTest {
    @Test
    public void test(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);//获得当前日期之前12个月的日期
        for (int i = 0; i < 12; i++) {
            //吧之前12个月放到list里面 只放到月份
            calendar.add(calendar.MONTH, 1);
            System.out.println(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }
    }

}
