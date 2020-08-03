package com.haowaner.quartz;

import com.haowaner.constant.RedisConstant;
import com.haowaner.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Set;

/**
 * 定时任务
 */
public class MyJob {
    @Autowired
    private JedisPool jedisPool;
    public void deleteJob(){
        //垃圾图片
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SET_PIC_CONSTANT, RedisConstant.SET_DB_PIC_CONSTANT);
        if(set!=null && set.size()>0) {
            for (String picName : set) {
                //删除七牛云看空间中的图片
                QiniuUtil.delete(picName);
                //删除redis中的垃圾数据
                jedisPool.getResource().srem(RedisConstant.SET_PIC_CONSTANT, picName);
                System.out.println(new Date());
            }
        }
        //System.out.println("Hello World "+new Date());
    }
}
