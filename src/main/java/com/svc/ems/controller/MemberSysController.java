package com.svc.ems.controller;

import com.svc.ems.dto.auth.*;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.JwtAuthLoginService;
import com.svc.ems.svc.auth.MemberAuthService;
import com.svc.ems.svc.base.CommonCodeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sys/member")
public class MemberSysController {


    private final MemberAuthService memberAuthService;
    private final JwtAuthLoginService jwtAuthLoginService;

    public MemberSysController(MemberAuthService memberAuthService, JwtAuthLoginService jwtAuthLoginService) {
        this.memberAuthService = memberAuthService;

        this.jwtAuthLoginService = jwtAuthLoginService;
    }

    @PostMapping("/login")
    @Operation(summary = "會員登入")
    public ResponseEntity<ApiResponseTemplate<String>> authLogin(@RequestBody LoginRequest req, HttpServletResponse response) {

        // 返回 JWT 和其他信息
        return jwtAuthLoginService.memberAuthLogin(req,response);
    }

    //會員註冊
    @PostMapping("/register")
    @Operation(summary = "會員註冊")
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(@RequestBody MemberRegisterRequest req) {

        // 返回 JWT 和其他信息
        return memberAuthService.memberRegister(req);
    }


    //會員驗證
    @PostMapping("/verify")
    @Operation(summary = "會員註冊驗證")
    public ResponseEntity<ApiResponseTemplate<String>> verifyEmail(@RequestBody Map<String, String> token, HttpServletResponse response) {
        return memberAuthService.verifyEmail(token, response);
    }

    //會員忘記密碼
    @PostMapping("/forgotPwd")
    @Operation(summary = "會員找回密碼")
    public ResponseEntity<ApiResponseTemplate<?>> memberForgotPwd(@RequestBody MemberPwdUpdateRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberForgotPwd(req, userDetails);
    }

    //會員重設密碼
    @PostMapping("/resetPwd")
    @Operation(summary = "會員重設密碼")
    public ResponseEntity<ApiResponseTemplate<?>> memberResetPwd(@RequestBody MemberResetPwdRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberResetPwd(req, userDetails);
    }



}
