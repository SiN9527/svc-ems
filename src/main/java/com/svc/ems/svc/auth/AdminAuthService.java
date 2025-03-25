package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.AdminRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

public interface AdminAuthService {



    public ResponseEntity<ApiResponseTemplate<?>> adminRegister(AdminRegisterRequest req);

    public ResponseEntity<ApiResponseTemplate<?>> adminLogin(AdminRegisterRequest req);

    public ResponseEntity<ApiResponseTemplate<?>> adminLogout();

    public ResponseEntity<ApiResponseTemplate<?>> adminRefreshToken(String refreshToken);

    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateProfile(AdminRegisterRequest req);


    public ResponseEntity<ApiResponseTemplate<?>> adminGetMemberList(AdminRegisterRequest req);

    public ResponseEntity<ApiResponseTemplate<?>> adminGetMemberProfile(AdminRegisterRequest req);

    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateMemberProfile(AdminRegisterRequest req);

    public ResponseEntity<ApiResponseTemplate<?>> adminDeleteMemberProfile(AdminRegisterRequest req);

    public ResponseEntity<ApiResponseTemplate<?>> adminUpdatePwd(AdminRegisterRequest req);
}
