package com.tensquare.sms.qa.config;

import com.tensquare.sms.qa.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//配置类  类似于xml的配置文件   在SpringBoot中对配置类配置
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport{

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**login/**/");
    }
}
