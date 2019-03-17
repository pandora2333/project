package com.dangdangnet.goods.controller;

import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GoodsExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(StatusCode.ERROR,e.getMessage(),null);
    }
}
