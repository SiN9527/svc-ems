package com.svc.ems.controller;

import com.svc.ems.dto.auth.*;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.MemberAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/verify")
    public ApiResponseTemplate<String> verifyEmail(@RequestParam("token") String token, HttpServletResponse response){
        return memberAuthService.verifyEmail(token, response);
    }

    @GetMapping("/profile")
    public ApiResponseTemplate<MemberProfileCookie> getMemberProfile(@CookieValue(value = "AUTH_TOKEN", required = false) String token){
        return memberAuthService.getMemberProfile(token);
    }

    @PostMapping("/updatePwd")

    public ApiResponseTemplate<?> memberUpdatePwd(@CookieValue(value = "AUTH_TOKEN", required = false)MemberPwdUpdateRequest req){
        return memberAuthService.memberUpdatePwd(req);
    }

}
