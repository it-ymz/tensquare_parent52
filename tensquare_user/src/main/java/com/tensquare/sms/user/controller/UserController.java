package com.tensquare.sms.user.controller;
import java.util.HashMap;
import java.util.Map;

import com.tensquare.sms.user.pojo.User;
import com.tensquare.sms.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JwtUtil jwtUtil;

	//粉丝和关注是成正比的，当A关注B的时候，那么A的关注数加1，那么B的粉丝数也加1

	/**
	 * 更新好友粉丝数和用户关注数
	 */
	@RequestMapping(value = "/{userid}/{friendid}/{x}",method = RequestMethod.PUT)
	public void updatefanscountandfollowcount(@PathVariable String userid,@PathVariable String friendid,@PathVariable int x){
		userService.updatefanscountandfollowcount(x,userid,friendid);
	}

	//登录
	@RequestMapping(value = "/login",method = RequestMethod.POST)
	public Result login(@RequestBody User user){
		User userlogin = userService.login(user.getMobile(),user.getPassword());
		if(userlogin==null){
			return new Result(false,StatusCode.LOGINERROR,"登录失败");
		}
		String token = jwtUtil.createJWT(userlogin.getId(), userlogin.getMobile(), "user");
		Map<String, Object> map = new HashMap<>();
		map.put("token",token);
		map.put("roles","user");
		return new Result(true,StatusCode.LOGINERROR,"登录成功",map);
	}

	//发送手机验证码
	@RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
	public Result sendSms(@PathVariable("mobile") String mobile){
		userService.sendSms(mobile);
		return  new Result(true,StatusCode.OK,"发送成功");
	}

	//用户注册
	@RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
	public Result regist(@PathVariable("code") String code,@RequestBody User user){
		//通过手机号得到redis缓冲中的验证码
		String checkCodeRedis = (String) redisTemplate.opsForValue().get("checkcode_"+user.getMobile());
		//判断缓冲中是否有这个值
		if(checkCodeRedis.isEmpty()){  //isEmpty判断是否为空
			return new Result(false,StatusCode.ERROR,"请先获取手机验证码");
		}
		//判断缓冲中的验证码与用户发来的验证码是否相同
		if(!checkCodeRedis.equals(code)){
			return new Result(false,StatusCode.ERROR,"请输入正确的验证码");
		}
		//添加到注册信息中
		userService.add(user);
		return new Result(true,StatusCode.OK,"注册成功");
	}

	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除  必须有adminn角色才能删除
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
