package com.dangdangnet.user.controller;
import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import com.dangdangnet.user.entity.User;
import com.dangdangnet.user.service.UserService;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Api(value = "微服务调用Api文档")
@Controller
@CrossOrigin//跨域请求
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;
    @RequestMapping(value = "/{userid}/{use}", method = RequestMethod.PUT)
    public void updateUser(@PathVariable("userid") String userid, @PathVariable(value = "use") String use_money){
        User user = userService.findById(userid);
        if(user == null||user.getFrozeen()==1) throw new RuntimeException("非法操作");
        BigDecimal money = new BigDecimal(use_money);
        if(money==null) throw new RuntimeException("非法操作");;
        BigDecimal temp = user.getUse_money().subtract(money);
        if(temp.floatValue()>=0.0f){
            user.setUse_money(temp);
        }
    }

    /**
     * 修改用户信息
     */
    @PostMapping(value = "/message/{userid}")
    public Result updateUserMesssage(@PathVariable("userid") String userid, @RequestBody User user) {
        User userQ = userService.findById(userid);
        if (user == null || user.getFrozeen() == 1) return new Result(StatusCode.LOGINERROR,"没有操作权限",user);
        if(StringUtils.isNotBlank(user.getAddress())) userQ.setAddress(user.getAddress());
        if(StringUtils.isNotBlank(user.getEmail())) userQ.setEmail(user.getEmail());
        if(StringUtils.isNotBlank(user.getAvatar())) userQ.setAvatar(user.getAvatar());
        if(StringUtils.isNotBlank(user.getMessage())) userQ.setMessage(user.getMessage());
        if(StringUtils.isNotBlank(user.getNickname())) userQ.setNickname(user.getNickname());
        if(StringUtils.isNotBlank(user.getUsername())) userQ.setUsername(user.getUsername());
        if(StringUtils.isNotBlank(user.getPhone())) userQ.setPhone(user.getPhone());
        if(user.getSex()!=null) userQ.setSex(user.getSex());
        if(StringUtils.isNotBlank(user.getPassword())) userQ.setPassword(user.getPassword());
        userService.save(user);
        return new Result(StatusCode.OK,"更新用户信息完毕",user);
    }
}
