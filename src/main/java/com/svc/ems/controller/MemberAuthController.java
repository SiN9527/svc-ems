package com.svc.ems.controller;

import com.svc.ems.dto.auth.MemberProfileResponse;
import com.svc.ems.dto.auth.MemberPwdUpdateRequest;
import com.svc.ems.dto.auth.MemberRegisterRequest;
import com.svc.ems.dto.auth.MemberResetPwdRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.common.CommonCodeList;
import com.svc.ems.dto.common.CommonCodeReqDTO;
import com.svc.ems.svc.auth.MemberAuthService;
import com.svc.ems.svc.base.CommonCodeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/member")
public class MemberAuthController {


    private final MemberAuthService memberAuthService;


    public MemberAuthController(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;

    }

    //會員登出
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    @Operation(summary = "會員登出")
    public ResponseEntity<ApiResponseTemplate<?>> memberLogout(HttpServletResponse response) {
        return memberAuthService.memberLogout(response);
    }

    //{ withCredentials: true }

    //取得個人資料
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    @Operation(summary = "登入者取得個人資料")
    public ResponseEntity<ApiResponseTemplate<MemberProfileResponse>> memberGetProfile(@AuthenticationPrincipal UserDetails userDetails) {
        return memberAuthService.memberGetProfile(userDetails);
    }

    // 修改密碼
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/updatePwd")
    @Operation(summary = "登入者修改密碼")
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(@RequestBody MemberPwdUpdateRequest req, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        return memberAuthService.memberUpdatePwd(req, userDetails, response);
    }

    //更新token
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/refreshToken")
    @Operation(summary = "登入者 刷新 Cookie 與Token ")
    public ResponseEntity<ApiResponseTemplate<?>> memberRefreshToken(@CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken,
                                                                     HttpServletResponse response) {
        return memberAuthService.memberRefreshToken(refreshToken, response);
    }

    ;

}
