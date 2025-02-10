package com.svc.ems.config.security;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.svc.ems.utils.JsonResult;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException, IOException {

        // 設定回應類型為 JSON，狀態碼為 401 未授權
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 建立自訂錯誤 JSON 響應
        JsonResult<Void> result = new JsonResult<>();
        result.setState(401);
        result.setMessage("Token is missing or invalid"); // 自訂錯誤訊息

        // 轉換為 JSON 字串並回傳
        String json = JSON.toJSONString(result);
        response.getWriter().write(json);
    }
}
