package com.tensquare.friend.interceptor;

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
        //得到请求的头部
        String header = request.getHeader("Authorization");
        //判断头部是否为空
        if(header!=null && header!=""){
            //透过判读签证的格式  前面是否是Bearer
            if(header.startsWith("Bearer ")){
                //格式否和，那么就将token取出来
                String token = header.substring(7);

                try {
                    //对令牌进行验证
                    Claims claims = jwtUtil.parseJWT(token);
                    String roles = (String) claims.get("roles");
                    if(roles!=null && roles.equals("admin")){
                        request.setAttribute("claims_admin",claims);
                    }
                    if(roles!=null && roles.equals("user")){
                        request.setAttribute("claims_user",claims);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("令牌不正确");
                }
            }
        }
        return true;
    }
}
