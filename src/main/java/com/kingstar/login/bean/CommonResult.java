package com.kingstar.login.bean;
import lombok.Data;

@Data
public class CommonResult<T> {


    private int code;
    private String message;
    private T data;

    public CommonResult<T> code(Integer code) {
        this.code = code;
        return this;
    }

    public CommonResult<T> message(String message) {
        this.message = message;
        return this;
    }

    public CommonResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public CommonResult() {
    }

    public static <T> CommonResult<T> success(T date) {
        return new CommonResult<T>().code(100).message("处理成功").data(date);
    }

    public static <T> CommonResult<T> fail(T date) {
        return new CommonResult<T>().code(200).message("处理失败").data(date);
    }

    public static <T> CommonResult<T> blank(int code, String message, T data) {
        return new CommonResult<T>().code(code).message(message).data(data);
    }

}