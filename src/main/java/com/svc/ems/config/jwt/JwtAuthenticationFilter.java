package com.svc.ems.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.enums.ErrorCode;
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
    private final ObjectMapper objectMapper = new ObjectMapper(); // **JSON 轉換工具**

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, UserDetailsService memberDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // **取得當前請求的 URI**
        String requestURI = request.getRequestURI();

        // **檢查是否是 Refresh Token 請求**
        boolean isRefreshRequest = requestURI.contains("/refreshToken");

        // **從 Cookie 或 Header 取得 Token**
        String token = getAuthToken(request, isRefreshRequest);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // **檢查 Token 是否過期**
            if (jwtUtil.isTokenExpired(token)) {
                clearAuthCookies(response);
                sendErrorResponse(response, "Your session has expired. Please log in again.");
                return;
            }

            // **解析 Token 取得 Email 和 用戶類型**
            String username = jwtUtil.extractUsername(token);
            String userType = jwtUtil.extractUserType(token);

            if (username != null && userType != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails;

                if ("ADMIN".equals(userType)) {
                    userDetails = userDetailsService.loadUserByUsername(username);
                } else if ("MEMBER".equals(userType)) {
                    userDetails = memberDetailsService.loadUserByUsername(username);
                } else {
                    sendErrorResponse(response, "Invalid user type.");
                    return;
                }

                boolean isValidToken = isRefreshRequest ? jwtUtil.validateRefreshToken(token) : jwtUtil.validateToken(token, userType);
                if (!isValidToken) {
                    sendErrorResponse(response, "Invalid or expired token.");
                    return;
                }

                // **將用戶資訊存入 SecurityContext**
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())
                );
            }
        } catch (Exception e) {
            sendErrorResponse(response, "Token validation failed: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getAuthToken(HttpServletRequest request, boolean isRefreshRequest) {
        String cookieName = isRefreshRequest ? "REFRESH_TOKEN" : "AUTH_TOKEN";

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        clearAuthCookies(response);

        ApiResponseTemplate<?> errorResponse = ApiResponseTemplate.fail(HttpServletResponse.SC_UNAUTHORIZED, message);
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
