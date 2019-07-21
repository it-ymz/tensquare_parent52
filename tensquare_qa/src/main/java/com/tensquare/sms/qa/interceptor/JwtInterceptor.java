package com.tensquare.sms.qa.interceptor;

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
        //通过requet获取到头
        String header = request.getHeader("Authorization");
        //首先是所有的请求都是要通过的，主要是对当前用户的角色进行拦截，不同的用户操作是不同的，所以需要判断头文件里面角色

        //判断头文件是否存在，存在那么就继续通过头得到角色
        if(header!=null && header!=""){
            //在加密的时候有一个约定就是，前面必须有 Bearer ，那么也可以通过判断这个头的字符串中是否存在这个约定
            if(header.startsWith("Bearer ")){
                //通过截取前七个的约定，那么后面就是服务端给客户端的签证
                String token = header.substring(7);
                try {
                    //得到签证中的信息
                    Claims claims = jwtUtil.parseJWT(token);
                    //获取用户角色
                    String  roles = (String) claims.get("roles");
                    if(roles!=null && roles.equals("admin")){
                        request.setAttribute("claims_user",token);
                    }
                    if(roles!=null && roles.equals("user")){
                        request.setAttribute("claims_user",token);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("令牌不正确");
                }
            }
        }
        return true;
    }
}
