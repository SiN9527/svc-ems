package com.svc.ems.config.jwt;

import java.util.List;

public abstract class BaseJwtService {

    private final JwtUtil jwtUtil;
    private final String type; // 記錄是 `USER` 或 `MEMBER`

    public BaseJwtService(JwtUtil jwtUtil, String type) {
        this.jwtUtil = jwtUtil;
        this.type = type;
    }

    // **產生 JWT Token**
    public String generateToken(String username, List<String> roles) {
        return jwtUtil.generateToken(username, type, roles);
    }

    // **驗證 Token 是否有效**
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token, type);
    }
}
