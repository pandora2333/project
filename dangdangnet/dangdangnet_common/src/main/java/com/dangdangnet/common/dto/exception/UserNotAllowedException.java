package com.dangdangnet.common.dto.exception;

/**
 * 针对用户权限不足，用户名/密码·错误
 * @author pandora2333
 */
public class UserNotAllowedException extends RuntimeException{

    public UserNotAllowedException(String msg){
        super(msg);
    }
}
