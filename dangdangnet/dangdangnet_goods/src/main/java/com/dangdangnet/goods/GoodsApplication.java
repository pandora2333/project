package com.dangdangnet.goods;

import com.dangdangnet.common.dto.util.IdWorker;
import com.dangdangnet.common.dto.util.JwtUtil;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * Goods货物展示模块服务
 * @author pandora2333
 */
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
public class GoodsApplication {
    @Bean
    public IdWorker idWorker(){
        return  new IdWorker(1,1);
    }
    @Bean
    public JwtUtil jwtUtil(){
        return  new JwtUtil();
    }
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class,args);
    }
}
