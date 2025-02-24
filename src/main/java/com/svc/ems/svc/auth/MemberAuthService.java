package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.*;
import com.svc.ems.dto.base.ApiResponseTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;

import java.util.Map;

public interface MemberAuthService {



    // 註冊
    public ApiResponseTemplate<String> memberRegister(MemberRegisterRequest req);

    // 驗證
    public ApiResponseTemplate<String> verifyEmail(String token, HttpServletResponse response);

    // cookie取得資料
    public ApiResponseTemplate<MemberProfileCookie> getMemberProfile(String token);

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
    public ApiResponseTemplate<?> memberUpdatePwd(@CookieValue(value = "AUTH_TOKEN", required = false) MemberPwdUpdateRequest req);

    public ApiResponseTemplate<?> memberForgotPwd(String email);

    public ApiResponseTemplate<?> memberResetPwd(String token, String password);


}
