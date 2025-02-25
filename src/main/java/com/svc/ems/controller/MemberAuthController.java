package com.svc.ems.controller;

import com.svc.ems.dto.auth.MemberProfileResponse;
import com.svc.ems.dto.auth.MemberPwdUpdateRequest;
import com.svc.ems.dto.auth.MemberRegisterRequest;
import com.svc.ems.dto.auth.MemberResetPwdRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.MemberAuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/member")
public class MemberAuthController {


    private final MemberAuthService memberAuthService;


    public MemberAuthController(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;

    }


    //會員註冊
    @PostMapping("/entrance/register")
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(@RequestBody MemberRegisterRequest req) {

        // 返回 JWT 和其他信息
        return memberAuthService.memberRegister(req);
    }

    //會員驗證
    @GetMapping("/entrance/verify")
    public ResponseEntity<ApiResponseTemplate<String>> verifyEmail(@RequestBody Map<String, String> token, HttpServletResponse response) {
        return memberAuthService.verifyEmail(token, response);
    }

    //會員忘記密碼
    @PostMapping("/entrance/forgotPwd")
    public ResponseEntity<ApiResponseTemplate<?>> memberForgotPwd(@RequestBody MemberPwdUpdateRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberForgotPwd(req, userDetails);
    }

    //會員重設密碼
    @PostMapping("/entrance/ResetPwd")
    public ResponseEntity<ApiResponseTemplate<?>> memberResetPwd(@RequestBody MemberResetPwdRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberResetPwd(req, userDetails);
    }

    //會員登出
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseTemplate<?>> memberLogout(HttpServletResponse response) {
        return memberAuthService.memberLogout(response);
    }

    //{ withCredentials: true }

    //取得個人資料
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public ResponseEntity<ApiResponseTemplate<MemberProfileResponse>> memberGetProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberGetProfile(userDetails);
    }

    // 修改密碼
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updatePwd")

    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(@RequestBody MemberPwdUpdateRequest req, @AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberUpdatePwd(req, userDetails);
    }

    //更新token
   @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponseTemplate<?>> memberRefreshToken(@CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken,
                                                                     HttpServletResponse response) {
    return memberAuthService.memberRefreshToken(refreshToken, response);
    }

    ;

}
