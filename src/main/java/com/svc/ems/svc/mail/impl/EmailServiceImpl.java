package com.svc.ems.svc.mail.impl;

import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.entity.EmailTemplateEntity;
import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.enums.EmailTypesEnum;
import com.svc.ems.repo.EmailTemplateRepository;
import com.svc.ems.svc.mail.EmailService;
import com.svc.ems.utils.MailTemplateUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailTemplateRepository emailTemplateRepository;
    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil; // JWT 工具類

    @Value("${spring.mail.username}")
    private String fromAddress;



    /**
     * 發送會員註冊驗證信
     *
     * @param email 收件者 Email
     */
    @Override
    public void sendVerificationEmail(String email) {
        // 產生 Email 驗證 Token，24 小時有效
        String verificationToken = jwtUtil.generateVerificationToken(email);

        //  準備驗證連結（本機環境）
        String verificationLink = "http://localhost:5173/verify?token=" + verificationToken;

        //  設定信件內容
        String subject = "Member Account Verification";
        String content = "<h3>Dear Member,</h3>"
                + "<p>Please click the link below to verify your account:</p>"
                + "<p><a href='" + verificationLink + "'>Click here to verify your account</a></p>"
                + "<p>This link will expire in 24 hours.</p>";

        //  發送 Email
        sendEmail(email, subject, content);
    }

    /**
     * 發送忘記密碼驗證信
     *
     * @param email 收件者 Email
     */

    @Override
    public void sendPasswordResetEmail(String email) {

        // 1️⃣ 產生密碼重設 Token，30 分鐘有效
        String resetToken = jwtUtil.generatePasswordResetToken(email);
        // 2️⃣ 對 email 進行 URL 編碼
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);

        // 3️⃣ 準備密碼重設連結，同時包含 token 與 email 參數
        String resetLink = "http://localhost:5173/resetPwd?token=" + resetToken;

        // 3️⃣ 設定信件內容
        String subject = "reset your password";
        String content = "<h3>Dear Member,</h3>"
                + "<p>Please click the link below to reset your password:</p>"
                + "<p><a href='" + resetLink + "'>Click here to reset your password</a></p>"
                + "<p>This link will expire in 30 minutes.</p>";

        // 4️⃣ 發送 Email
        sendEmail(email, subject, content);
    }

    @Override
    public void sendPasswordChangedNotification(MemberMainEntity member) {
        // 1️⃣ 設定信件內容
        String subject = "Password Changed Notification";
        String content = "<h3>Dear " + member.getFirstName() + " " + member.getLastName() + ",</h3>"
                + "<p>Your password has been successfully changed.</p>"
                + "<p>If you did not make this change, please contact us immediately.</p>";

        // 2️⃣ 發送 Email
        sendEmail(member.getEmail(), subject, content);
    }

    /**
     * **發送 Email 的共用方法**
     *
     * @param to      收件者 Email
     * @param subject 郵件主題
     * @param content 郵件內容（HTML 格式）
     */
    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // 使用 HTML 格式

            mailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }

    // EmailService 寄送暫時密碼通知
    public void sendTempPasswordEmail(MemberMainEntity member, String tempPassword) {
        String subject = "Temporary Password for AASD 2025";
        String content = """
                <p>Dear %s,</p>
                <p>You requested to reset your password. Please use the following temporary password to log in:</p>
                <p><b>%s</b></p>
                <p>⚠️ For security reasons, you must reset your password immediately after logging in.</p>
                """.formatted(member.getLastName(), tempPassword);
        sendEmail(member.getEmail(), subject, content);

    }


    /**
     * 根據活動、範本類型、參數與收件者，寄出動態信件內容
     * 僅允許 ADMIN 發送
     */
    @Override
    public void sendTemplateMail(String eventId, EmailTypesEnum template, Map<String, String> params, String toEmail, UserDetails sender) {
//        boolean isAdmin = sender.getAuthorities().stream()
//                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
//
//        if (!isAdmin) {
//            throw new SecurityException("Only admin can send templated emails.");
//        }

        Optional<EmailTemplateEntity> optional = emailTemplateRepository.findByEventIdAndTemplateType(eventId, template.getType());
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Email template not found for type: " + template.name());
        }

        EmailTemplateEntity emailTemplate = optional.get();

        String subject = MailTemplateUtils.fillTemplate(emailTemplate.getSubject(), params);
        String content = MailTemplateUtils.fillTemplate(emailTemplate.getContent(), params);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setFrom(fromAddress);
            mailSender.send(message);
            log.info("Email sent to {} with template {}", toEmail, template.name());
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}