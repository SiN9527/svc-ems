package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

public interface UserAuthService {

    public ResponseEntity<?> login(UserLoginRequest req);

    public ResponseEntity<ApiResponseTemplate<String>> userRegister(UserRegisterRequest req);
}
