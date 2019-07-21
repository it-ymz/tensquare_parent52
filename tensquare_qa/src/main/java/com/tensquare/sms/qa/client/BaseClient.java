package com.tensquare.sms.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient()注解是指定从哪个服务中调用功能  里面中的值是使用的每一个应用服务的名称
@FeignClient("tensquare-base")
public interface BaseClient { //需要在问答里面调用base，那么就可以在问答里面有一个接口

   @RequestMapping(value = "/label/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable("labelId") String id);
}
