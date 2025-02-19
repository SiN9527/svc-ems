package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.MemberRegisterRequest;
import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface MemberAuthService {



    // 註冊
    public ApiResponseTemplate<String> memberRegister(MemberRegisterRequest req);

    // 驗證
    public ApiResponseTemplate<String> verifyEmail(Map<String, String> req);

    // 忘記密碼
    public ApiResponseTemplate<?> memberFindPwd(UserLoginRequest req);

    // 登出
    public ApiResponseTemplate<?> memberLogout();

    // 更新Token
    public ApiResponseTemplate<?> memberRefreshToken();

    // 取得個人資料
    public ApiResponseTemplate<?> memberGetProfile();

    // 更新個人資料
    public ApiResponseTemplate<?> memberUpdateProfile();

    // 更新密碼
    public ApiResponseTemplate<?> memberUpdatePwd();

    public ApiResponseTemplate<?> memberForgotPwd(String email);

    public ApiResponseTemplate<?> memberResetPwd(String token, String password);


}
