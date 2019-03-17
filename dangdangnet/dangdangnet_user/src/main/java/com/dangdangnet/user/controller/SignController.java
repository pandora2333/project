package com.dangdangnet.user.controller;

import com.dangdangnet.common.dto.Result;
import com.dangdangnet.common.dto.StatusCode;
import com.dangdangnet.common.dto.util.FileUtils;
import com.dangdangnet.common.dto.util.IdWorker;
import com.dangdangnet.common.dto.util.JwtUtil;
import com.dangdangnet.user.entity.Admin;
import com.dangdangnet.user.entity.User;
import com.dangdangnet.user.service.AdminService;
import com.dangdangnet.user.service.UserService;
import com.dangdangnet.user.util.FastDFSClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 账户登录/注册
 * @author pandora2333
 */
@Api(value = "登录/注册Api文档")
@RestController
@CrossOrigin//跨域请求
@RequestMapping("/sign/")
@Slf4j
public class SignController {

    @Resource
    UserService userService;
    @Resource
    AdminService adminService;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    IdWorker idWorker;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    FastDFSClient fastDFSClient;
    @Value("${pic.path}")
    String path;

    @ApiOperation(value = "用户登录(填写用户名/密码)", notes = "测试登陆后返回信息", response = Result.class)
    @PostMapping("inUser")
    public Result signInUser(@RequestBody User user) {

        User userLogin = userService.login(user);
        if (userLogin == null || userLogin.getFrozeen() == 1 || !encoder.matches(user.getPassword(), userLogin.getPassword())|| userLogin.getFrozeen()==1) {
            return new Result<>(StatusCode.LOGINERROR, "登录失败", user);
        }
        //使得前后端可以通话的操作，jwt令牌
        //令牌签发
        String token = jwtUtil.createJWT(userLogin.getId(), userLogin.getUsername(), "user");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("role", "user");
        //缓存登录id
        redisTemplate.opsForValue().set(userLogin.getId(), "user_" + userLogin.getUsername(), 1, TimeUnit.HOURS);
        return new Result<>(StatusCode.OK, "登录成功", map);
    }

    @ApiOperation(value = "管理员登录(填写用户名/密码)", notes = "测试登陆后返回信息", response = Result.class)
    @PostMapping("inAdmin")
    public Result signInAdmin(@RequestBody Admin admin) {

        Admin adminLogin = adminService.login(admin);
        if (adminLogin == null || !encoder.matches(admin.getPassword(), adminLogin.getPassword())|| adminLogin.getFrozeen()==1) {
            return new Result<>(StatusCode.LOGINERROR, "登录失败", admin);
        }
        //使得前后端可以通话的操作，jwt令牌
        //令牌签发
        String token = jwtUtil.createJWT(adminLogin.getId(), adminLogin.getAdminname(), "admin");
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("role", "admin");
        //缓存登录id
        redisTemplate.opsForValue().set(adminLogin.getId(), "admin_" + adminLogin.getAdminname(), 1, TimeUnit.HOURS);
        return new Result<>(StatusCode.OK, "登录成功", map);
    }

    @ApiOperation(value = "用户注册(all)", notes = "测试登陆后返回信息")
    @PostMapping("register")
    public Result register(@RequestBody User user) {
        if (user != null) {
            User exists = userService.findByUsername(user.getUsername());
            if(exists!=null) return new Result<>(StatusCode.ERROR, "用户已存在，不能重复添加",user);
            user.setDate(new Date());//创建账户时间
            user.setId(String.valueOf(idWorker.nextId()));
            String originPwd = user.getPassword();
            user.setPassword(encoder.encode(originPwd));
            user.setFrozeen(1);//需要邮箱验证激活
            if(StringUtils.isNotBlank(user.getAvatar())){
                String pic_path = path+user.getId()+".png";
                String up_pic = pic_path;
                try {
                    FileUtils.base64ToFile(pic_path,user.getAvatar());
                    up_pic = fastDFSClient.uploadFile(FileUtils.fileToMultipart(pic_path));
                    new File(pic_path).delete();
                } catch (Exception e) {
                    log.info("error in base-save:{}", e);
                    return new Result(StatusCode.ERROR, "图片解析失败", null);
                }
                user.setAvatar(up_pic);//数据库中保存图片网络地址
            }
            userService.save(user);
            user.setPassword(originPwd);
            //发送邮件
            // 使用rabbitmq发送邮件验证
            if (StringUtils.isNotBlank(user.getEmail())) {
                sendMsg(user);
                //缓存邮件key，过期需重发
                redisTemplate.opsForValue().set("user_"+user.getId(), "user_" + user.getUsername(), 30, TimeUnit.MINUTES);
            } else {
                return new Result(StatusCode.ERROR, "邮箱非法", user);
            }
            return new Result(StatusCode.OK, "注册成功,请尽快邮箱激活", user);
        }
        return new Result(StatusCode.ERROR, "注册错误", null);
    }

    /**
     * 单播（点对点）
     */
    @Async
    public void sendMsg(User user) {
        rabbitTemplate.convertAndSend("pandora.fanout", "pandora.news", user);
    }

    /**
     * 邮件直连激活使用
     *
     * @param token
     * @return
     */
    @ApiOperation(value = "用户激活账号", notes = "测试登陆后返回信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType="query", name = "token", value = "token", required = true, dataType = "String")
    })
    @GetMapping("/Reg")
    public Result reg(@RequestParam(name = "token", defaultValue = "-1") String token) {
        User user = userService.findById(token);
        if (user != null && user.getFrozeen() == 1 && redisTemplate.hasKey("user_"+user.getId())) {
            user.setFrozeen(0);
            userService.updateByFrozeen(user);
            redisTemplate.delete("user_"+user.getId());
            return new Result(StatusCode.OK, "激活成功，快去登陆吧！",null);
        }
        return new Result(StatusCode.ACCESSERROR, "激活失败！", user);

    }
}


