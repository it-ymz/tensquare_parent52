package com.tensquare.sms.spit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication  //@SpringBootApplication扫描的是本包下所有的注解
public class SpitApplication {

    public static void main(String[] args) {  //启动类
        SpringApplication.run(SpitApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
