package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.UserLoginResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;

public interface JwtAuthLoginService {

    public ApiResponseTemplate<UserLoginResponse> authLogin(LoginRequest req);


}
