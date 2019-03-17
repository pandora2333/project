package com.dangdangnet.common.dto;

import lombok.Data;

/**
 * 封装响应体数据
 * @author pandora2333
 * @param <T>
 */
@Data
public class Result<T> {
    T data;//响应体数据
    int code;//状态码
    String message;//响应信息
    public Result(int code,String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
