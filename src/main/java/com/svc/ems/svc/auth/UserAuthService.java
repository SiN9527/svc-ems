package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

public interface UserAuthService {



    public ApiResponseTemplate<?> userRegister(UserRegisterRequest req);
}
