package com.svc.ems.svc.mail;

import com.svc.ems.entity.MemberMainEntity;

public interface EmailService {
    public void  sendVerificationEmail(String email);
    public void sendPasswordResetEmail(String email);
    void sendTempPasswordEmail(MemberMainEntity member, String tempPassword);

    void sendPasswordChangedNotification(MemberMainEntity member);
}
