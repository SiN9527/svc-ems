package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.MemberLoginRequest;
import com.svc.ems.dto.auth.MemberRegisterRequest;
import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

public interface MemberAuthService {



    // 註冊
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(MemberRegisterRequest req);

    // 忘記密碼
    public ResponseEntity<?> memberFindPwd(UserLoginRequest req);

    // 登出
    public ResponseEntity<?> memberLogout();

    // 更新Token
    public ResponseEntity<?> memberRefreshToken();

    // 取得個人資料
    public ResponseEntity<?> memberGetProfile();

    // 更新個人資料
    public ResponseEntity<?> memberUpdateProfile();

    // 更新密碼
    public ResponseEntity<?> memberUpdatePwd();


}
