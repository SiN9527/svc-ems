package com.svc.ems.config.jwt;

import org.springframework.stereotype.Service;


@Service
public class UserJwtService extends BaseJwtService {
    public UserJwtService(JwtUtil jwtUtil) {
        super(jwtUtil, "USER");
    }
}
