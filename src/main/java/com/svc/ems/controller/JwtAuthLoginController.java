package com.svc.ems.controller;

import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.svc.auth.JwtAuthLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/common")
public class JwtAuthLoginController {


    private final JwtAuthLoginService jwtAuthLoginService;


    public JwtAuthLoginController(JwtAuthLoginService jwtAuthLoginService) {

        this.jwtAuthLoginService = jwtAuthLoginService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> authLogin(@RequestBody LoginRequest req) {

        // 返回 JWT 和其他信息
        return jwtAuthLoginService.authLogin(req);
    }
}
