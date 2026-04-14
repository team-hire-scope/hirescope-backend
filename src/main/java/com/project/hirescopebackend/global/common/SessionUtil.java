package com.project.hirescopebackend.global.common;

import com.project.hirescopebackend.global.exception.BusinessException;
import com.project.hirescopebackend.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 세션에서 로그인 사용자 ID를 추출하는 공통 유틸
 * 모든 인증이 필요한 Controller에서 사용
 */
public class SessionUtil {

    public static final String SESSION_USER_ID = "userId";

    private SessionUtil() {}

    /**
     * 세션에서 userId를 꺼냄. 세션 없거나 userId 없으면 401 예외 발생
     */
    public static Long getLoginUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        Long userId = (Long) session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return userId;
    }
}
