package com.svc.ems.controller;

import com.svc.ems.dto.auth.AdminRegisterRequest;
import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.AdminAuthService;
import com.svc.ems.svc.auth.JwtAuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sys/admin")
public class UserAuthController {


    private final AdminAuthService adminAuthService;
    private final JwtAuthLoginService jwtAuthLoginService;


    public UserAuthController(AdminAuthService adminAuthService, JwtAuthLoginService jwtAuthLoginService) {
        this.adminAuthService = adminAuthService;

        this.jwtAuthLoginService = jwtAuthLoginService;
    }

    @PostMapping("/login")
    @Operation(summary = "管理者登入")
    public ResponseEntity<ApiResponseTemplate<String>> authLogin(@RequestBody LoginRequest req, HttpServletResponse response) {

        // 返回 JWT 和其他信息
        return jwtAuthLoginService.adminAuthLogin(req,response);
    }

//    @PostMapping("/register")
//    @Operation(summary = "用戶註冊")
//    public ResponseEntity<ApiResponseTemplate<?>> adminRegister(@RequestBody AdminRegisterRequest req) {
//        // 返回 JWT 和其他信息
//        return adminAuthService.userRegister(req);
//    }
}
