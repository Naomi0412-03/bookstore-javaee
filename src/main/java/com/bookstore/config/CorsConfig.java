package com.bookstore.config;

import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")//只对/api开头的接口生效
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")//允许请求头
                .allowCredentials(false)//是否允许携带cookie
                .maxAge(3600);//预检请求缓存时间
    }
}
