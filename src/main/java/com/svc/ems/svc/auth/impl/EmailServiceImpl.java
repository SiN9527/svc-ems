package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.svc.auth.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

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
        // 1️⃣ 產生 Email 驗證 Token，24 小時有效
        String verificationToken = jwtUtil.generateVerificationToken(email);

        // 2️⃣ 準備驗證連結
        String verificationLink = "https://your-domain.com/verify?token=" + verificationToken;

        // 3️⃣ 設定信件內容
        String subject = "會員帳戶驗證";
        String content = "<h3>親愛的會員，</h3>"
                + "<p>請點擊下方鏈接來驗證您的帳戶：</p>"
                + "<p><a href='" + verificationLink + "'>點此驗證 Email</a></p>"
                + "<p>此驗證連結 24 小時內有效。</p>";

        // 4️⃣ 發送 Email
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

        // 2️⃣ 準備密碼重設連結
        String resetLink = "https://your-domain.com/reset-password?token=" + resetToken;

        // 3️⃣ 設定信件內容
        String subject = "密碼重設請求";
        String content = "<h3>親愛的會員，</h3>"
                + "<p>我們收到您的密碼重設請求，請點擊下方鏈接來重設您的密碼：</p>"
                + "<p><a href='" + resetLink + "'>點此重設密碼</a></p>"
                + "<p>此連結 30 分鐘內有效，如果您沒有請求重設密碼，請忽略此郵件。</p>";

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
