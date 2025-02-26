package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.AdminRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

public interface AdminAuthService {



    public ResponseEntity<ApiResponseTemplate<?>> userRegister(AdminRegisterRequest req);
}
