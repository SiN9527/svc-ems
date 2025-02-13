package com.svc.ems.controller;

import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.svc.auth.UserAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserAuthController {


    private final UserAuthService userAuthService;


    public UserAuthController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequest req) {

        // 返回 JWT 和其他信息
        return userAuthService.login(req);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest req) {

        // 返回 JWT 和其他信息
        return userAuthService.userRegister(req);
    }
}
