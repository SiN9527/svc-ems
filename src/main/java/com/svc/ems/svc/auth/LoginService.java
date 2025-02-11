package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface LoginService {

    public ResponseEntity<?> login(LoginRequest loginRequest);
}
