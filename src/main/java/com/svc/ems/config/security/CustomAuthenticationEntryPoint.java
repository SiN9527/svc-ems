package com.svc.ems.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svc.ems.dto.base.ApiResponseTemplate;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.UUID;

/**
 * **自定義未授權 (401 Unauthorized) 回應處理**
 * - 當用戶請求需要身份驗證的資源但未提供有效的 Token 時，觸發此處理
 */
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        log.warn("Unauthorized request to: {} | Reason: {}", request.getRequestURI(), authException.getMessage());

        String errorMessage = "Unauthorized";
        clearAuthCookies(response);

        ApiResponseTemplate<Void> errorResponse = ApiResponseTemplate.<Void>builder()
                .httpStatusCode(HttpStatus.UNAUTHORIZED.value())
                .errorCode("UNAUTHORIZED")
                .message(errorMessage)
                .success(false)
                .path(request.getRequestURI())
                .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private void clearAuthCookies(HttpServletResponse response) {
        Cookie authCookie = new Cookie("AUTH_TOKEN", null);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setPath("/");
        authCookie.setMaxAge(0);
        response.addCookie(authCookie);
    }
}