package com.example.ecommercesystemproject.common.config;

import com.example.ecommercesystemproject.admin.dto.AdminSession;
import com.example.ecommercesystemproject.admin.entity.Role;
import com.example.ecommercesystemproject.admin.entity.Status;
import com.example.ecommercesystemproject.common.ServiceException;
import com.example.ecommercesystemproject.common.annotation.UserInfo;
import com.example.ecommercesystemproject.common.constant.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@AllArgsConstructor
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasParam = parameter.hasParameterAnnotation(UserInfo.class);
        boolean isAdminSession = parameter.getParameterType() == AdminSession.class;

        return hasParam && isAdminSession;
    }

    @Override
    public @Nullable Object resolveArgument(
            MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null)
            throw new ServiceException("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);

        HttpSession session = request.getSession(false);
        if (session == null)
            throw new ServiceException("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);

        Long sessionAdminId = (Long) session.getAttribute(SessionConst.LOGIN_ADMIN_ID);
        Role sessionAdminRole = (Role) session.getAttribute(SessionConst.LOGIN_ADMIN_ROLE);
        String sessionAdminEmail = (String) session.getAttribute(SessionConst.LOGIN_ADMIN_EMAIL);
        Status sessionAdminStatus = (Status) session.getAttribute(SessionConst.LOGIN_ADMIN_STATUS);

        if (sessionAdminId == null)
            throw new ServiceException("로그인이 필요합니다.", HttpStatus.UNAUTHORIZED);

        if (sessionAdminRole.equals(Role.NONE))
            throw new ServiceException("권한이 없습니다.", HttpStatus.FORBIDDEN);

        if (!sessionAdminStatus.equals(Status.ACTIVE))
            throw new ServiceException("계정이 비활성화 상태입니다.", HttpStatus.FORBIDDEN);

        return new AdminSession(
                sessionAdminId,
                sessionAdminRole,
                sessionAdminEmail,
                sessionAdminStatus
        );
    }
}
