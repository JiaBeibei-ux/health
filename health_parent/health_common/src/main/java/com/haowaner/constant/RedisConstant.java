package com.haowaner.constant;

/**
 * redis常量信息
 */
public class RedisConstant {
    public static final String  SET_DB_PIC_CONSTANT="数据库中的图片";//用于redis中的图片存到数据库
    public static final String  SET_PIC_CONSTANT="redis中的图片";//用于redis中的图片
    public static final String SENDTYPE_ORDER = "001";//用于缓存体检预约时发送的验证码
    public static final String SENDTYPE_LOGIN = "002";//用于缓存手机号快速登录时发送的验证码
    public static final String SENDTYPE_GETPWD = "003";//用于缓存找回密码时发送的验证码
}
