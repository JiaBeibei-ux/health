package com.haowaner.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-redis.xml")
public class JobTest {
    @Test
    public void test() throws InterruptedException {
        System.out.println(".....");
        Thread.sleep(10000);
    }
}
