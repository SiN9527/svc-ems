package com.svc.ems.config.jwt;

import org.springframework.stereotype.Service;

@Service
public class MemberJwtService extends BaseJwtService {
    public MemberJwtService(JwtUtil jwtUtil) {
        super(jwtUtil, "MEMBER");
    }
}