package com.svc.ems.controller;

import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.UserLoginResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.JwtAuthLoginService;
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
    public ApiResponseTemplate<UserLoginResponse> authLogin(@RequestBody LoginRequest req) {

        // 返回 JWT 和其他信息
        return jwtAuthLoginService.authLogin(req);
    }
}
