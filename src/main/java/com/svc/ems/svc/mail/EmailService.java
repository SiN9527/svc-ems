package com.svc.ems.svc.mail;

public interface EmailService {
    public void  sendVerificationEmail(String email);
    public void sendPasswordResetEmail(String email);


}
