package com.svc.ems.config.jwt;

import org.springframework.stereotype.Service;


@Service
public class AdminJwtService extends BaseJwtService {
    public AdminJwtService(JwtUtil jwtUtil) {
        super(jwtUtil, "USER");
    }
}
