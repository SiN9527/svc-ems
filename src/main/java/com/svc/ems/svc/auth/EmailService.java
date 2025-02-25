package com.svc.ems.svc.auth;

public interface EmailService {
    public void  sendVerificationEmail(String email);
    public void sendPasswordResetEmail(String email);


}
