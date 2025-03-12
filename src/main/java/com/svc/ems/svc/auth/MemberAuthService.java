package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.*;
import com.svc.ems.dto.base.ApiResponseTemplate;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface MemberAuthService {


    // 註冊
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(MemberRegisterRequest req);

    // 驗證
    public ResponseEntity<ApiResponseTemplate<String>> verifyEmail(@RequestBody Map<String, String> token, HttpServletResponse response);

    // cookie取得資料
    public ResponseEntity<ApiResponseTemplate<MemberProfileResponse>> memberGetProfile(UserDetails userDetails);

    // 忘記密碼
    public ResponseEntity<ApiResponseTemplate<?>> memberFindPwd(AdminLoginRequest req);

    // 登出
    public ResponseEntity<ApiResponseTemplate<?>> memberLogout(HttpServletResponse response);


    // 更新Token
    public ResponseEntity<ApiResponseTemplate<?>> memberRefreshToken(String refreshToken, HttpServletResponse response);

    // 更新個人資料
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdateProfile(MemberUpdateRequest req, UserDetails userDetails,HttpServletResponse response);

    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(MemberPwdUpdateRequest req, UserDetails userDetails,HttpServletResponse response);

    public ResponseEntity<ApiResponseTemplate<?>> memberForgotPwd(MemberPwdUpdateRequest req, UserDetails userDetails);

    public ResponseEntity<ApiResponseTemplate<?>> memberResetPwd(MemberResetPwdRequest req, UserDetails userDetails);


}
