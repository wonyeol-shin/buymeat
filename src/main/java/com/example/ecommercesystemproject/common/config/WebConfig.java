package com.example.ecommercesystemproject.common.config;

import com.example.ecommercesystemproject.common.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserInfoArgumentResolver userInfoArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1) // // 인터셉터가 여러 개일 때 실행 순서 (숫자가 작을수록 먼저 실행)
                .addPathPatterns("/api/**"); // // /api로 시작하는 모든 URL에 적용
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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userInfoArgumentResolver);
    }
    
}
