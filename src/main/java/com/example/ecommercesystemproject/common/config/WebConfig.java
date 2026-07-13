package com.example.ecommercesystemproject.common.config;

import com.example.ecommercesystemproject.common.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/api/**");
                /* 요청으로 인한 삭제
                // signup(/signup), login(/login)이 이제 /api 밖에 있어서
                // 애초에 /api/** 패턴에 안 걸림 -> exclude 자체가 필요 없어짐
                .excludePathPatterns(
                        "/api/signup",
                        "/api/login",
                        "/api/logout"
                );
                */
    }
}
