package com.dangdangnet.goods.controller;

import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import com.dangdangnet.goods.annotation.LoginAuthc;
import com.dangdangnet.goods.entity.Order;
import com.dangdangnet.goods.service.FlowService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 物流信息逻辑处理
 * @author pandora2333
 */
@RestController
@CrossOrigin
@RefreshScope
@RequestMapping("/flow")
public class FlowController {

    @Resource
    FlowService flowService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @PostMapping("order")//下订单
    @LoginAuthc
    public Result orderGoods(@RequestBody Order[] orders, HttpServletRequest request){
        String user = (String)request.getAttribute("claims_user");
        String id = (String)request.getAttribute("claims_user_id");
        if(StringUtils.isBlank(user)||StringUtils.isBlank(id)) return new Result(StatusCode.LOGINERROR,"用户身份校验失败,请重新登录",null);
        //获取缓存登录id
        String claim = redisTemplate.opsForValue().get(id);
        if(StringUtils.isNotBlank(claim)&&claim.equals("user_"+user)){
                flowService.insertAll(orders,user,id);
            return new Result(StatusCode.OK,"购物清单提交完毕",user);
        }
        return new Result(StatusCode.LOGINERROR,"请先登录",null);
    }

    @PostMapping("updateOrder")//修改订单
    @LoginAuthc
    public Result updateGoods(@RequestBody Order[] orders, HttpServletRequest request){
        String user = (String)request.getAttribute("claims_user");
        String id = (String)request.getAttribute("claims_user_id");
        if(StringUtils.isBlank(user)||StringUtils.isBlank(id)) return new Result(StatusCode.LOGINERROR,"用户身份校验失败,请重新登录",null);
        //获取缓存登录id
        String claim = redisTemplate.opsForValue().get(id);
        if(StringUtils.isNotBlank(claim)&&claim.equals("user_"+user)){
            flowService.updateGoods(orders);
            return new Result(StatusCode.OK,"购物清单修改完毕",user);
        }
        return new Result(StatusCode.LOGINERROR,"请先登录",null);
    }

    @PostMapping("delGoods")//删除订单
    @LoginAuthc
    public Result delGoods(@RequestBody Order[] orders, HttpServletRequest request){
        String user = (String)request.getAttribute("claims_user");
        String id = (String)request.getAttribute("claims_user_id");
        if(StringUtils.isBlank(user)||StringUtils.isBlank(id)) return new Result(StatusCode.LOGINERROR,"用户身份校验失败,请重新登录",null);
        //获取缓存登录id
        String claim = redisTemplate.opsForValue().get(id);
        if(StringUtils.isNotBlank(claim)&&claim.equals("user_"+user)){
            flowService.delGoods(orders);
            return new Result(StatusCode.OK,"购物清单删除完毕",user);
        }
        return new Result(StatusCode.LOGINERROR,"请先登录",null);
    }

}
