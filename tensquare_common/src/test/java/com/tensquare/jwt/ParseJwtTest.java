package com.tensquare.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.text.SimpleDateFormat;

public class ParseJwtTest {
    public static void main(String[] args) {


        Claims claims = Jwts.parser()  //使用Jwts.parser()验证签名
                .setSigningKey("itcast")  //setSingngKey()是设置加盐的
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLlsI_pqawiLCJpYXQiOjE1NjMzNTQ5OTYsImV4cCI6MT" +
                        "U2MzM1NTA1Niwicm9sZSI6ImFkbWluIn0.nNJxfKjmoUBzpvH-PiwFT2FZzIXz5sXNyU2S8aY3UZo") //parseClaimsJws()是设置签证的
                .getBody();
        System.out.println("用户id:"+claims.getId()); //获取签证中的id号
        System.out.println("用户名:"+claims.getSubject());//获取签证中的用户名
        System.out.println("登录时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getIssuedAt())); //获取签证中的登录时间

        try {
            System.out.println("过期时间:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(claims.getExpiration())); //expiration()过期时间
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("用户角色："+claims.get("role"));
    }
}
