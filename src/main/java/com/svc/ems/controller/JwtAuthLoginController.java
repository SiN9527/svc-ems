package com.svc.ems.controller;

import com.svc.ems.dto.auth.AdminLoginResponse;
import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.JwtAuthLoginService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/common")
public class JwtAuthLoginController {


    private final JwtAuthLoginService jwtAuthLoginService;


    public JwtAuthLoginController(JwtAuthLoginService jwtAuthLoginService) {

        this.jwtAuthLoginService = jwtAuthLoginService;
    }


    @PostMapping("/login")
    @Operation(summary = "共用登入")
    public ResponseEntity<ApiResponseTemplate<String>> authLogin(@RequestBody LoginRequest req, HttpServletResponse response) {

        // 返回 JWT 和其他信息
        return jwtAuthLoginService.authLogin(req,response);
    }
}
