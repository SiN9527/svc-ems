package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.*;
import com.svc.ems.dto.base.ApiResponseTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

public interface MemberAuthService {


    // 註冊
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(MemberRegisterRequest req);

    // 驗證
    public ResponseEntity<ApiResponseTemplate<String>> verifyEmail(String token, HttpServletResponse response);

    // cookie取得資料
    public ResponseEntity<ApiResponseTemplate<MemberProfileResponse>> memberGetProfile(@CookieValue(value = "AUTH_TOKEN", required = false) String token);

    // 忘記密碼
    public  ResponseEntity<ApiResponseTemplate<?>> memberFindPwd(UserLoginRequest req);

    // 登出
    public ResponseEntity<ApiResponseTemplate<?>> memberLogout(HttpServletResponse response);

    // 更新Token
    public ResponseEntity<ApiResponseTemplate<?>> memberRefreshToken();

    // 更新個人資料
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdateProfile();

    // 更新密碼
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(@CookieValue(value = "AUTH_TOKEN", required = false) MemberPwdUpdateRequest req);

    public ResponseEntity<ApiResponseTemplate<?>> memberForgotPwd(MemberPwdUpdateRequest req);

    public ResponseEntity<ApiResponseTemplate<?>> memberResetPwd(@RequestBody MemberResetPwdRequest req);


}
