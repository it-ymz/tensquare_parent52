package com.tensquare.sms.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.jws.Oneway;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

import com.tensquare.sms.user.dao.UserDao;
import com.tensquare.sms.user.pojo.User;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import util.JwtUtil;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired   //BCryptPasswordEncoder 加密
	private BCryptPasswordEncoder encoder;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private JwtUtil jwtUtil;

	/**
	 * 增加
	 * @param user
	 */
	public void add(User user) {
		//将用户第一次注册的信息初始化
		user.setId( idWorker.nextId()+"" );
		//密码加密
		user.setPassword(encoder.encode(user.getPassword()));
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(new Date());//注册日期
		user.setUpdatedate(new Date());//更新日期
		user.setLastdate(new Date());//最后登录日期
		userDao.save(user);
	}

	//发送验证码
	public void sendSms(String mobile) {
		//生成六位随机数
		String checkcode = RandomStringUtils.randomNumeric(6);
		//往缓冲中放一份
		redisTemplate.opsForValue().set("checkcode_"+mobile,checkcode,6, TimeUnit.HOURS);
		Map<String ,String> map = new HashMap<>();
		map.put("mobile",mobile);
		map.put("checkcode",checkcode);
		//给用户发一份
		//rabbitTemplate.convertAndSend("sms",map);
		//在控制台显示一份【方便测试】
		System.out.println("验证码："+checkcode);
	}

	//用户登录
	public User login(String mobile,String password){
		User user = userDao.findByMobile(mobile);
		if(user!=null && encoder.matches(password,user.getPassword())){
			return user;
		}
		return null;
	}
	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除   删除用户的话，那么必须需要管理员来进行删除 admin
	 * @param id
	 */
	public void deleteById(String id) {
		/*String header = request.getHeader("Authorization");
		if(header==null || header == ""){
			//当头文件里面为空的话，那么证明证明没有得到令牌，就需要直接抛出异常，走一个运行时异常会直接进行处理，并且在controller也已经定义过异常处理类，所以可以直接抛出
			throw new RuntimeException("权限不足");
		}
		//判断header的开头是否是Bearer 开头的，这是一种前后端的约定
		if(!header.startsWith("Bearer ")){
			throw new RuntimeException("权限不足");
		}
		//得到token
		String token = header.substring(7);

		try {
			//通过工具类来验证客户端传来的token是否一致 然后返回claim
			Claims claims = jwtUtil.parseJWT(token);
			String roles = (String) claims.get("roles");//通过claim得到设置token中的信息
		    if(roles==null && !roles.equals("admin")){
		    	throw new RuntimeException("权限不足");
			}
		} catch (Exception e) {
			throw new RuntimeException("权限不足");
		}*/
		String token = (String) request.getAttribute("claims_admin");
		if(token==null || token== ""){
			throw new RuntimeException("权限不足");
		}
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 登陆名称
                if (searchMap.get("loginname")!=null && !"".equals(searchMap.get("loginname"))) {
                	predicateList.add(cb.like(root.get("loginname").as(String.class), "%"+(String)searchMap.get("loginname")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // 电话号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	@Transactional
    public void updatefanscountandfollowcount(int x, String userid, String friendid) {
    	userDao.updatefanscount(x,friendid); //更新好友粉丝数
    	userDao.updatefollowcount(x,userid);//更新用户关注数
	}
}
