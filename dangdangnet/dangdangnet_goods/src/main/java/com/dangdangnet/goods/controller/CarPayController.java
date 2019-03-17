package com.dangdangnet.goods.controller;

import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import com.dangdangnet.goods.annotation.LoginAuthc;
import com.dangdangnet.goods.service.CarService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 购物车支付逻辑处理
 * @author pandora2333
 */
@RestController
@CrossOrigin
@RefreshScope
@RequestMapping("/car")
public class CarPayController {

    @Resource
    CarService carService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @PostMapping
    @LoginAuthc
    public Result payGoods(HttpServletRequest request){
        String user = (String)request.getAttribute("claims_user");
        String id = (String)request.getAttribute("claims_user_id");
        if(StringUtils.isBlank(user)||StringUtils.isBlank(id)) return new Result(StatusCode.LOGINERROR,"用户身份校验失败,请重新登录",null);
        //获取缓存登录id
        String claim = redisTemplate.opsForValue().get(id);
        if(StringUtils.isNotBlank(claim)&&claim.equals("user_"+user)){
            carService.payMoney(user,id);
        }
        return new Result(StatusCode.OK,"购物车支付完毕",user);
    }

}
