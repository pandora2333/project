package com.dangdangnet.user.service.impl;

import com.dangdangnet.common.dto.util.MD5Utils;
import com.dangdangnet.user.dao.UserDao;
import com.dangdangnet.user.entity.User;
import com.dangdangnet.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * 账户service层实现
 * @author pandora2333
 */
@Transactional
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;
    @Resource
    JavaMailSenderImpl javaMailSender;
    @Override
    public User login(User user) {
        return userDao.findUserByUsername(user.getUsername());
    }

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public User findById(String userid) {
        return userDao.findById(userid).get();
    }

    @Override
    public void updateByFrozeen(User user) {
        userDao.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    /**
     * 发送邮件
     * @param user
     */
    @RabbitListener(queues = {"pandora.news"})
    public void receive(User user){//自动反序列化成Book对象
        try {
            complexSMail(user);
        } catch (Exception e) {
            log.error("邮件发送异常:{}",e.getMessage());
        }
    }

    @Value("${mail.sendFrom}")
    String mail;
    @Async
    public void complexSMail(User user) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message,true);
            //mulitpart:true 在文件传输时被需要
            helper.setSubject("Westlake International - Application Received");
            helper.setText("<html><meta http-equiv=content-type content=text/html; charset=utf-8/> <p><img alt=\"westlakelogo\" src=\"http://www.expertsonchina.com/images/top.gif\" border=\"0\" /></p>"
                    +"<p>邮件验证， 亲爱的 master:"+user.getNickname()+",<br />" +
                    "<br />" +
                    "	欢迎您，您已经成功和我们签订了魔法契约!快来网站battle(购买)吧!。</p></br>" +"<a href=http://47.107.55.172:8989/muxin/u/Reg.do?token="+user.getNickname()+"&tokenconf=token&mid="+ MD5Utils.getMD5Str(""+System.currentTimeMillis())+">点击激活账号</a>"+
                    "	<p>We'd like to thank you for your interest in our expert   network business. We appreciate you taking time to apply for membership in our   expert community.</p>" +
                    "	<p>To ensure the integrity and quality of our network, we seek to verify the   credentials of our experts. We hope that you understand it. We will send you a   confirmation email shortly.</p>" +
                    "	<p>Best regards,<br />" +"	  <br />	Westlake International Team </p>	<p>&nbsp;</p></html>",true);
            log.info("sendFrom:{}",mail);
            helper.setFrom(mail);
            helper.setTo(user.getEmail());
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error("邮件发送异常:{}",e.getMessage());
            return;
        }
        log.info("邮件发送正常,账户:{}",user.getUsername());
    }

}
