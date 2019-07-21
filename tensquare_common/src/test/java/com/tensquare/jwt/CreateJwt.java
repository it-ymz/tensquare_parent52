package com.tensquare.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

//创建JWT
public class CreateJwt {
    //Jwts中builder()是生成签名  parser()是验证签名
    public static void main(String[] args) {
        JwtBuilder jwtBuilder = Jwts.builder()
                .setId("666") //设置id号
                .setSubject("小马") //设置名称
                .setIssuedAt(new Date()) //设置时间戳
                .signWith(SignatureAlgorithm.HS256,"itcast") //设置头  SignatureAlgorithm.HS256(hs256算法) itcast盐
                .setExpiration(new Date(new Date().getTime()+60000))  //设置过期时间
                .claim("role","admin");  //自定义信息
        System.out.println(jwtBuilder.compact()); //jwtBuilder.compact()生成的就是一个字符串
        //使用过期时间 setExpiration
        //自定义内容：claim("","")
    }
}
