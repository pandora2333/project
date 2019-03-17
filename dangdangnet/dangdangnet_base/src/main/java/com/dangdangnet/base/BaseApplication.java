package com.dangdangnet.base;

import com.dangdangnet.common.dto.util.IdWorker;
import com.dangdangnet.common.dto.util.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;

/**
 * 基础微服务模块,后台管理模块
 * @author pandora2333
 */
@SpringBootApplication
@EnableEurekaClient
@EnableHystrixDashboard
public class BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class, args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }
    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

}
