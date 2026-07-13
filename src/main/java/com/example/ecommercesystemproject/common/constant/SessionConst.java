package com.example.ecommercesystemproject.common.constant;

public class SessionConst {

    private SessionConst() {

    }

    public static final String LOGIN_ADMIN_ID = "LOGIN_ADMIN_ID";
    public static final String LOGIN_ADMIN_EMAIL = "LOGIN_ADMIN_EMAIL";
    public static final String LOGIN_ADMIN_ROLE = "LOGIN_ADMIN_ROLE";

    public static final int SESSION_TIMEOUT_SECONDS = 60 * 60 * 24; // 세션 유효시간 : 24시간(초 단위)
}
