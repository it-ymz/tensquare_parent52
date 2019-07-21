package com.tensquare.sms.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication //启动类
@EnableEurekaClient
public class BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseApplication.class,args);
    }

    @Bean  //bean是将需要使用的类加载到spring容器中
    public IdWorker idWorker(){   //微服务需要将分布式id生成器加入Bean中
        return new IdWorker(1,1);
    }
}
