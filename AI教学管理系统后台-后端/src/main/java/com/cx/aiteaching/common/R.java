package com.cx.aiteaching.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装返回统一数据格式
 */
public class R extends HashMap<String,Object> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int SUCCESS_CODE = 200;
    private static final int FAILURE_CODE = 400;

    private static final int ERROR_CODE = 500;
    private static final String SUCCESS_MSG_STR = "操作成功";
    private static final String FAILURE_MSG_STR = "操作失败";
    private static final String ERROR_MSG_STR = "系统繁忙，请稍后重试！";

    /**
     * 有参构造器
     * @param code 响应状态码
     * @param msg 响应提示信息
     * @param data 响应具体业务数据
     */
    public R(int code,String msg,Object data) {
        super(3); // 继承自Map，设置初始容量大小3
        this.put("code", code); // 响应状态码
        this.put("msg", msg); // 响应提示信息
        this.put("data", data); // 响应具体业务数据
    }

    /**
     * 有参构造器
     * @param code 响应状态码
     * @param msg 响应提示信息
     */
    public R(int code,String msg) {
        super(2); // 继承自Map，设置初始容量大小2
        this.put("code", code); // 响应状态码
        this.put("msg", msg); // 响应提示信息
    }

    /**
     * 添加额外的键值对
     * @param key 键
     * @param value 值
     * @return 返回额外的数据
     */
    @Override
    public R put(String key,Object value) {
        super.put(key, value);
        return this;
    }

    public static R ok(Object data) {

        return new R(SUCCESS_CODE,SUCCESS_MSG_STR,data);
    }

    public static R ok(String msg) {
        return new R(SUCCESS_CODE,msg);
    }

    public static R ok(String msg,Object data) {
        return new R(SUCCESS_CODE,msg,data);
    }


    public static R error(String msg) {
        return new R(ERROR_CODE,msg);
    }

    public static R error(String msg,Object data) {
        return new R(ERROR_CODE,msg,data);
    }

    public static R error() {
        return new R(ERROR_CODE,ERROR_MSG_STR);
    }


    public static R fail(String msg) {


        return new R(FAILURE_CODE,msg);
    }
    public static R fail(String msg,int code) {
        return new R(code,msg);
    }

    public static R fail() {


        return new R(FAILURE_CODE,FAILURE_MSG_STR);
    }

    /**
     * 快速生成一个Map对象
     * @param key 键
     * @param value 值
     * @return
     */
    public static Map<String,Object> map(String key,Object value) {
        Map<String,Object> data = new HashMap<>(1);
        data.put(key, value);
        return data;
    }

}
