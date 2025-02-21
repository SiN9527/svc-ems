package com.svc.ems.controller;

import com.svc.ems.dto.auth.MemberRegisterRequest;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.dto.auth.VerifyRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.MemberAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/member")
public class MemberAuthController {


    private final MemberAuthService memberAuthService;


    public MemberAuthController(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;

    }


    @PostMapping("/register")
    public ApiResponseTemplate<String> memberRegister(@RequestBody MemberRegisterRequest req) {

        // 返回 JWT 和其他信息
        return memberAuthService.memberRegister(req);
    }

    @PostMapping("/verify")
    public ApiResponseTemplate<String> verifyEmail(@RequestBody VerifyRequest req){
        return memberAuthService.verifyEmail(req);
    }
}
