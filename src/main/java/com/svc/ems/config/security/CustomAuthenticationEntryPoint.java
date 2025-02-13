package com.svc.ems.config.security;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svc.ems.dto.base.ApiResponse;
import jakarta.servlet.ServletException;
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

    private final ObjectMapper objectMapper = new ObjectMapper(); // **JSON 轉換工具**

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.warn("Unauthorized request to: {}", request.getRequestURI()); // **記錄未授權請求**

        // **構建 API 錯誤回應**
        ApiResponse<Void> errorResponse = ApiResponse.<Void>builder()
                .httpStatusCode(HttpStatus.UNAUTHORIZED.value()) // **401 狀態碼**
                .error("UNAUTHORIZED")  // **錯誤類型**
                .messageDetail("Token is missing or invalid") // **錯誤訊息**
                .path(request.getRequestURI()) // **請求的 API 路徑**
                .traceId(UUID.randomUUID().toString()) // **生成唯一請求識別碼**
                .build();

        // **設定回應類型 & 狀態碼**
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // **將 `ApiResponse` 轉換為 JSON 回應**
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}