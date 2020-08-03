package com.haowaner.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.haowaner.constant.MessageConstant;
import com.haowaner.constant.RedisConstant;
import com.haowaner.entity.PageResult;
import com.haowaner.entity.QueryPageBean;
import com.haowaner.entity.Result;
import com.haowaner.pojo.Setmeal;
import com.haowaner.service.SetmealService;
import com.haowaner.utils.QiniuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 会员套餐管理控制器
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private SetmealService setmealService;

    /**
     * 图片上传值服务器
     * @param imgFile 文件名称
     * @return
     * 使用七牛云的工具类 参数字节流 和 文件名使用UUID随机产生的
     * 这里也要判断一下文件的后缀和大小
     */
    @RequestMapping("/upload")
    public Result qiniuUpload(@RequestParam(value="imgFile")MultipartFile imgFile){
        //图片原始名
        String originalFilename = imgFile.getOriginalFilename();
        //获取后缀名.jpg
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+suffix;
        try {
            QiniuUtil.upload(imgFile.getBytes(),fileName);
            //将图片名称存到redis中
            //先将无论保存到数据库没有图片放到redis
            jedisPool.getResource().sadd(RedisConstant.SET_PIC_CONSTANT,fileName);
            //将redis中保存图片的两个数据比较一下 得到插值
//            Set<String> sdiff = jedisPool.getResource().sdiff(RedisConstant.SET_PIC_CONSTANT,RedisConstant.SET_DB_PIC_CONSTANT);
            /*for (String large : sdiff) {
                 //使用定时任务删除垃圾图片
            }*/
            return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 回显数据需要
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(@RequestParam(value ="id") Integer id){
        Setmeal setmeal;
        try {
            setmeal = setmealService.findById(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    /**
     * 回显检查组信息
     * @return
     */
    @RequestMapping("/findByCheckGroupId")
    public Result findByCheckGroupId(@RequestParam(value ="id") Integer id){
        try {
            List<Integer> checkGroupIds = setmealService.findByCheckGroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 添加方法
     * @param setmeal
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.add(setmeal,checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 展示数据
     * @param queryPageBean 分页及模糊查询的参数
     * @return
     */
    @RequestMapping("/list")
    public Result list(@RequestBody QueryPageBean queryPageBean){
        try {
            PageResult pageResult = setmealService.list(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    //编辑 先删除所有 最后 axios.post("/setmeal/checkgroupId?checkgroupIds="+this.checkgroupIds).then(res=>{
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkgroupIds){
        try {
            setmealService.edit(setmeal,checkgroupIds);
            return new Result(true,"编辑套餐管理成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"编辑套餐管理失败");
        }
    }

    @RequestMapping("/delete")
    public Result delete(@RequestParam(value ="id") Integer id){
        try {
            setmealService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_MEMBER_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_MEMBER_SUCCESS);
    }
}
