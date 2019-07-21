package com.tensquare.sms.spit.controller;

import com.tensquare.sms.spit.pojo.Spit;
import com.tensquare.sms.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin  //解决跨域问题
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    //@Qualifier()  这个注解和autowired一块使用就是按名称注入
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id){
        return new Result(true,StatusCode.OK,"查询成功",spitService.findById(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit){
        spitService.save(spit);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    @RequestMapping(method = RequestMethod.PUT,value = "/{id}")
    public Result update(@RequestBody Spit spit,@PathVariable("id") String id){
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修该成功");
    }

    @RequestMapping(method = RequestMethod.DELETE,value = "/{id}")
    public Result delete(@PathVariable("id") String id){
        spitService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    //在使用分页的时候，需要使用到Pageable类，springdata都是使用pageable进行分页  pageable的size和page参数都是使用的int类型
    //
    @RequestMapping(method = RequestMethod.GET,value = "/comment/{parentid}/{page}/{size}")
    public Result findByParentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size){
        Page<Spit> pageable = spitService.findByParentid(parentid, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(pageable.getTotalElements(),pageable.getContent()));
    }

    /**
     *控制不能重复点赞:
     * 使用redis技术：当点赞成功以后，往redis中存放一个标识，证明这个用户已经点过赞
     * 然后在controller的点赞方法中，先判断redis中这个用户的是否点过赞，点过赞就不能在点了
     * 如果没有点过赞，那么就执行点赞代码块，并且等点赞成功以后，给当前用户的redis做一个表示，证明已经点过赞
     *
     */
    //点赞
    @RequestMapping(method = RequestMethod.PUT,value = "/thumbup/{spitId}")
    public Result thumbup(@PathVariable("spitId") String spitId){
        //判断当前用户是否已经点过赞，但是现在我们没有做认证，暂时先把userid写死
        String userid = "111";
        //判断当前用户是否已经点过赞
        if(redisTemplate.opsForValue().get("thumbup_"+userid)!=null){
            return new Result(false,StatusCode.OK,"不能重复点赞");
        }
        //执行点赞
        spitService.thumbup(spitId);
        //设置当前用户已经点过赞
        redisTemplate.opsForValue().set("thumbup_"+userid,1);
        return new Result(true,StatusCode.OK,"点赞成功");
    }
}
