package com.svc.ems.controller;

import com.svc.ems.config.jwt.MemberJwtService;
import com.svc.ems.config.jwt.UserJwtService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserJwtService userJwtService;
    private final MemberJwtService memberJwtService;

    public AuthController(UserJwtService userJwtService, MemberJwtService memberJwtService) {
        this.userJwtService = userJwtService;
        this.memberJwtService = memberJwtService;
    }

    @PostMapping("/user/login")
    public String userLogin(@RequestParam String username, @RequestParam List<String> roles) {
        return userJwtService.generateToken(username, roles); // 生成管理員 Token
    }

    @PostMapping("/member/login")
    public String memberLogin(@RequestParam String username, @RequestParam List<String> roles) {
        return memberJwtService.generateToken(username, roles); // 生成會員 Token
    }
}
