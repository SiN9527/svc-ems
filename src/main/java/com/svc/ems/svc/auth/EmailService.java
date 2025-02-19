package com.svc.ems.svc.auth;

import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;

public interface EmailService {
    public void  sendVerificationEmail(String email);
    public void sendPasswordResetEmail(String email);


}
