package com.tensquare.sms.rabbit.customer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "itcast")
public class Customer1 {
    @RabbitHandler
    public void getMst(String msg){
        System.out.println("itcast:"+msg);
    }
}
