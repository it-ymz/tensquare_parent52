package com.tensquare.sms.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component
public class JwtInterceptor implements HandlerInterceptor{

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了拦截器");
        //无论如何都放行。具体能不能操作还是在具体的操作中去判断
        //拦截器只是负责把头请求中包含token的令牌进行一个解析验证

        /**
         * 拦截器主要的的实现：
         *     只要登录成功，是对所有的用户都进行放行的。但是如果想要执行里面的操作，那么还需要判断该用户是否有头
         *     并且头中包含的角色是否能执行多种操作，所以说，该拦截器是对所有的用户放行，但能否执行其中的命令还需要判断用户头中的角色
         */

        String header = request.getHeader("Authorization");

        if(header!=null && !"".equals(header)){
            //如果有包含Authorization头信息，就对其进行解析
            if(header.startsWith("Bearer ")){
                //如果头信息包含了Bearer 的话，那么认为是满足认证头信息的规则
                
                //得到Token
                String token = header.substring(7);
                //对令牌进行验证
                try {
                    Claims claims = jwtUtil.parseJWT(token);
                    //当令牌验证通过，那么就可以获取当前用户的角色
                    String roles = (String) claims.get("roles");
                    if(roles!=null && roles.equals("admin")){
                        //如果用户是admin角色的话，那么就可以将admin用户存入到request作用域中，方便调用
                        request.setAttribute("claims_admin",token);
                    }
                    if(roles!=null && roles.equals("user")){
                        request.setAttribute("claims_user",token);
                    }
                }catch (Exception e){
                        throw new RuntimeException("令牌不正确");
                }

            }
        }
        return true;
    }
}
