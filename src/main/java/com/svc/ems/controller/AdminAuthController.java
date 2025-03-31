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
public class AdminAuthController {


    private final AdminAuthService adminAuthService;
    private final JwtAuthLoginService jwtAuthLoginService;


    public AdminAuthController(AdminAuthService adminAuthService, JwtAuthLoginService jwtAuthLoginService) {
        this.adminAuthService = adminAuthService;

        this.jwtAuthLoginService = jwtAuthLoginService;
    }


}
