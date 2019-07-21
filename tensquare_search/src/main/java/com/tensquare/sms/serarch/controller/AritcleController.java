package com.tensquare.sms.serarch.controller;

import com.tensquare.sms.serarch.pojo.Article;
import com.tensquare.sms.serarch.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class AritcleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Article article){
        articleService.save(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    //查询  根据es搜索引擎进行搜索
    @RequestMapping(method = RequestMethod.GET,value = "/{key}/{page}/{size}")
    public Result findByKey(@PathVariable("key") String key,@PathVariable("page") int page,@PathVariable("size") int size){
        Page<Article> pageData = articleService.findByKey(key,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Article>(pageData.getTotalElements(),pageData.getContent()));
    }
}
