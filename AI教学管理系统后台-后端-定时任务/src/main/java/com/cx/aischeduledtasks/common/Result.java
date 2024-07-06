package com.cx.aischeduledtasks.common;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Result implements Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;
    private int code;
    private Object data;
    private String timestamp;

    private Result() {
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    private Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    }

    public static Result success() {
        return new Result(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMsg());
    }

    public static Result success(Object data) {
        return new Result(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMsg(), data);
    }

    public static Result success(Object data, String msg) {
        return new Result(CodeEnum.SUCCESS.getCode(), msg, data);
    }

    public static Result fail() {
        return new Result(CodeEnum.EXCEPTION.getCode(), CodeEnum.EXCEPTION.getMsg());
    }

    public static Result fail(CodeEnum responseEnums) {
        return new Result(responseEnums.getCode(), responseEnums.getMsg());
    }

    public static Result fail(CodeEnum responseEnums, Object data) {
        return new Result(responseEnums.getCode(), responseEnums.getMsg(), data);
    }

    public static Result fail(int code, String msg) {
        return new Result(code, msg);
    }

    public static Result fail(int code, String msg, Object data) {
        return new Result(code, msg, data);
    }

}
