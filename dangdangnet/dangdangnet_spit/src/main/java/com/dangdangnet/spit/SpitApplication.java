package com.dangdangnet.spit;

import com.dangdangnet.common.dto.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

/**
 * Mongodb存储吐槽信息
 */
@SpringBootApplication
@EnableEurekaClient
public class SpitApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpitApplication.class);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1, 1);
    }
}
