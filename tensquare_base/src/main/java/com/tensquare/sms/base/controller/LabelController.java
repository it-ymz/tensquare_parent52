package com.tensquare.sms.base.controller;

import com.tensquare.sms.base.pojo.Label;
import com.tensquare.sms.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin  //解决跨域问题
@RestController  //RestController 是ResponseBody 和 Controller整合到一块的  以后就不需要响应前端写@ResponseBody
@RequestMapping("label")
public class LabelController {

    @Autowired
    private LabelService labelService;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findAll());
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{labelId}")  //{}表示的站位符
    public Result findById(@PathVariable("labelId") String labelId){
        System.out.println("22222222");
        return new Result(true,StatusCode.OK,"查询成功",labelService.findById(labelId));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label label){
        labelService.save(label);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.PUT)  //method是指访问的方式是什么，value是表示这个请求是有参数的，然后使用站位符来接收  {}中的名字必须要和参数中的@PathVariable()名称要一样
    public Result update(@PathVariable("labelId") String labelId,@RequestBody Label label){
        label.setId(labelId);
        labelService.update(label);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    @RequestMapping(value = "/{labelId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable("labelId") String labelId){
       labelService.deleteById(labelId);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    //@RequestBody注解可以让json数据转为javaBean的一种数据类型，还可以转为Map数据类型
    @RequestMapping(value = "/search",method = RequestMethod.POST)
    public Result findSearch(@RequestBody Label label){
        List<Label> list = labelService.findSearch(label);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    @RequestMapping(value = "/search/{page}/{size}",method = RequestMethod.POST)
    public Result pageQuery(@RequestBody Label label,@PathVariable int page,@PathVariable int size){
        Page<Label> pageData = labelService.pageQuery(label,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));
    }
}
