package com.svc.ems.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;  // **用於 USER 認證**
    private final UserDetailsService memberDetailsService;  // **用於 MEMBER 認證**

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService, UserDetailsService memberDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // **從 Header 取得 Token**
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // **去掉 "Bearer " 前綴**
        String username = jwtUtil.extractUsername(token); // **解析用戶名**
        String userType = jwtUtil.extractUserType(token); // **解析身份類型（USER / MEMBER）**

        if (username != null && userType != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails;

            if ("USER".equals(userType)) {
                userDetails = userDetailsService.loadUserByUsername(username);  // **處理 `USER`**
            } else if ("MEMBER".equals(userType)) {
                userDetails = memberDetailsService.loadUserByUsername(username);  // **處理 `MEMBER`**
            } else {
                filterChain.doFilter(request, response);
                return;  // **身份類型不合法，直接放行**
            }

            if (jwtUtil.validateToken(token, userType)) {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities())

                );
            }
        }
        filterChain.doFilter(request, response);
    }
}
