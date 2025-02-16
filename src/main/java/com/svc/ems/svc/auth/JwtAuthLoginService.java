package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

public interface JwtAuthLoginService {

    public ResponseEntity<?> authLogin(LoginRequest req);


}
