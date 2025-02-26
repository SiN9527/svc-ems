package com.svc.ems.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svc.ems.dto.base.ApiResponseTemplate;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserDetailsService memberDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, UserDetailsService memberDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
    }

    /**
     * **核心方法：過濾器邏輯**
     * 這個方法會在每個請求進來時執行，檢查 JWT Token，並設定用戶身份。
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // **從 Cookie 或 Header 取得 Token**

        String requestURI = request.getRequestURI(); // 取得請求的 URL 路徑

        // **🔍 檢查是否是 refresh token 請求**
        boolean isRefreshRequest = requestURI.contains("/auth/member/refresh");

        // **根據 API 選擇適當的 Token 來驗證**
        String token = getAuthToken(request, isRefreshRequest);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }


        try {
            // **解析 Token 取得使用者名稱 (Email)**
            String username = jwtUtil.extractUsername(token);

            // **解析 Token 取得用戶類型 (USER / MEMBER)**
            String userType = jwtUtil.extractUserType(token);

            // **確保 SecurityContext 還未設定身份，避免重複驗證**
            if (username != null && userType != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;

                // **根據 userType 決定查詢哪個 UserDetailsService**
                if ("USER".equals(userType)) {
                    userDetails = userDetailsService.loadUserByUsername(username);
                } else if ("MEMBER".equals(userType)) {
                    userDetails = memberDetailsService.loadUserByUsername(username);
                } else {
                    // **如果身份類型不合法，直接回傳錯誤響應**
                    request.setAttribute("TOKEN_ERROR", "Invalid user type.");
                    filterChain.doFilter(request, response);
                    return;
                }
                // **🔍 根據 API 選擇驗證方式**
                boolean isValidToken = isRefreshRequest ? jwtUtil.validateRefreshToken(token) : jwtUtil.validateToken(token, userType);
                // **驗證 Token 是否有效**
                if (!isValidToken) {
                    request.setAttribute("TOKEN_ERROR", "Invalid or expired token.");
                    filterChain.doFilter(request, response);
                    return;
                }

                // **將用戶資訊存入 SecurityContext，Spring Security 會識別這個身份**
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                );
            }
        } catch (Exception e) {
            // **處理 Token 解析錯誤，例如格式錯誤、過期等**
            request.setAttribute("TOKEN_ERROR", "Token validation failed: " + e.getMessage());
            return;
        }

        // **繼續執行其他過濾器**
        filterChain.doFilter(request, response);
    }

    /**
     * **從請求中取得 JWT Token**
     * Token 優先從 Cookie 讀取，若 Cookie 沒有，再從 `Authorization` Header 讀取
     */
    private String getAuthToken(HttpServletRequest request, boolean isRefreshRequest) {
        String cookieName = isRefreshRequest ? "REFRESH_TOKEN" : "AUTH_TOKEN";

        // **1️⃣ 嘗試從 Cookie 取得 Token**
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // **2️⃣ 若 Cookie 沒有，再從 Header 讀取**
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) { // **標準格式：`Bearer <token>`**
            return authHeader.substring(7); // **去掉 "Bearer " 前綴，取得真正的 Token**
        }

        return null; // **若都找不到 Token，回傳 null**
    }

    /**
     * **發送錯誤響應**
     * 當 Token 無效時，直接回傳 HTTP 錯誤訊息，避免 API 進一步執行。
     */
    /**
     * **發送錯誤響應 (使用 ApiResponseTemplate)**
     * 當 Token 無效或驗證失敗時，統一回傳 JSON 格式的錯誤訊息。
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 設定 HTTP 狀態碼
        response.setContentType("application/json"); // 設定回應類型為 JSON

        // **如果是 Token 過期，清除 Cookie**
        if ("Invalid or expired token.".equals(message) || "TOKEN_EXPIRED".equals(message)) {
            Cookie authCookie = new Cookie("AUTH_TOKEN", null);
            authCookie.setHttpOnly(true);
            authCookie.setSecure(true);
            authCookie.setPath("/");
            authCookie.setMaxAge(0); // **設置 MaxAge 為 0，立即刪除**
            response.addCookie(authCookie);

            // **回傳「請重新登入」的訊息**
            message = "Your session has expired. Please log in again.";
        }

        // 使用 ApiResponseTemplate 產生標準錯誤回應
        ApiResponseTemplate<?> errorResponse = ApiResponseTemplate.fail(HttpServletResponse.SC_UNAUTHORIZED, message);

        // 轉換成 JSON 字串並輸出
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

}
