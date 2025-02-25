package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.AdminLoginResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface JwtAuthLoginService {

    public ResponseEntity<ApiResponseTemplate<AdminLoginResponse>> authLogin(LoginRequest req, HttpServletResponse response);


}
