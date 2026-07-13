package com.example.ecommercesystemproject.common.interceptor;

import com.example.ecommercesystemproject.common.constant.SessionConst;
import com.example.ecommercesystemproject.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_ADMIN_ID) == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        return true;
    }
}
