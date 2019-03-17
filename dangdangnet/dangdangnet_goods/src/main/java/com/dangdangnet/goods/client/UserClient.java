package com.dangdangnet.goods.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 基于restful风格的微服务调用
 */
@Component
@FeignClient("dangdangnet-user")
public interface UserClient {
    @RequestMapping(value = "/user/{userid}/{use}", method = RequestMethod.PUT)
    public boolean updateUser(@PathVariable("userid") String userid, @PathVariable("use") String use_money);

}
