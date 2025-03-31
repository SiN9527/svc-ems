package com.svc.ems.controller;

import com.svc.ems.dto.auth.*;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.AdminAuthService;
import com.svc.ems.svc.auth.JwtAuthLoginService;
import com.svc.ems.svc.auth.MemberAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/sys/admin")
public class AdminSysController {


    private final AdminAuthService adminAuthService;
    private final JwtAuthLoginService jwtAuthLoginService;

    public AdminSysController(AdminAuthService adminAuthService, JwtAuthLoginService jwtAuthLoginService) {
        this.adminAuthService = adminAuthService;

        this.jwtAuthLoginService = jwtAuthLoginService;
    }

    @PostMapping("/login")
    @Operation(summary = "管理員登入")
    public ResponseEntity<ApiResponseTemplate<String>> authLogin(@RequestBody AdminLoginRequest req, HttpServletResponse response) {

        // 返回 JWT 和其他信息
        return jwtAuthLoginService.adminAuthLogin(req,response);
    }

    //會員註冊
    @PostMapping("/register")
    @Operation(summary = "管理員註冊")
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(@RequestBody AdminRegisterRequest req) {

        // 返回 JWT 和其他信息
        return adminAuthService.adminRegister(req);
    }




}
