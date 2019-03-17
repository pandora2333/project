package com.dangdangnet.user;
import com.dangdangnet.common.dto.util.IdWorker;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * user模块服务
 * @author pandora2333
 */
@SpringBootApplication
@EnableEurekaClient
@EnableAsync
@EnableRabbit
public class UserApplication {
    @Bean
    public IdWorker idWorker(){
        return  new IdWorker(1,1);
    }
    @Bean
    public BCryptPasswordEncoder encoder(){
        return  new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
