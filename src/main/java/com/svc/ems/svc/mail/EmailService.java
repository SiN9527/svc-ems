package com.svc.ems.svc.mail;

import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.enums.EmailTypesEnum;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface EmailService {
    public void  sendVerificationEmail(String email);
    public void sendPasswordResetEmail(String email);
    void sendTempPasswordEmail(MemberMainEntity member, String tempPassword);

    void sendPasswordChangedNotification(MemberMainEntity member);

    void sendTemplateMail(String eventId, EmailTypesEnum type, Map<String, String> params, String toEmail, UserDetails sender);
}
