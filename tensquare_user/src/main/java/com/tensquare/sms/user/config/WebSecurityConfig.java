package com.tensquare.sms.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@Configuration  //@Configuration注解表示的是当前修饰的类是配置类
@EnableWebSecurity
//添加注解的使用是远远不够用的，还需要继承WebSecurityConfigurerAdapter来自动加载配置文件
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{  //这个类中提供了很多的没有实现的空方法，当我们使用的话，那么直接实现一个想用的方法使用进行

    //当需要使用Spring Security安全配置的时候，那么就实现这个configure的配置文件
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authorizeRequests所有security全注解配置实现的开端(也就是需要使用安全配置的时候authorizeRequests是它的开头)，表示开始说明需要的权限。
        //需要的权限分为两部分，第一部分是拦截的路径，第二部分访问路径需要的权限
        //写authorizeRequests()这个方法说明以下的方法都是来分配权限
        //antMatchers表示拦截什么路径，permitAll()任何权限都可以访问，直接可以放行所有
        //anyRequest()任何的请求，authenticated认证后才能访问
        //.and().csrf().disable()，固定写法，表示使csrf拦截失效
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
                //csrf是一种网络攻击技术,也就是spring security设置的安全级别要高，它的意思就是说除了内部请求以外的请求都会认为是恶意攻击
    }
}