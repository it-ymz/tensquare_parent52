package com.tensquare.sms.rabbit.customer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "kudingyu")
public class Customer3 {
    @RabbitHandler
    public void getMst(String msg){

        System.out.println("kudingyu:"+msg);
    }
}
