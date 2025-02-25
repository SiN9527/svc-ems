package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.UserLoginResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface JwtAuthLoginService {

    public ResponseEntity<ApiResponseTemplate<UserLoginResponse>> authLogin(LoginRequest req, HttpServletResponse response);


}
