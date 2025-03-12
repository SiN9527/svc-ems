package com.svc.ems.svc.mail.impl;

import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.svc.mail.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final JwtUtil jwtUtil; // JWT 工具類

    public EmailServiceImpl(JavaMailSender mailSender, JwtUtil jwtUtil) {
        this.mailSender = mailSender;
        this.jwtUtil = jwtUtil;
    }


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

}
