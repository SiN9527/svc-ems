package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.AdminLoginResponse;
import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.entity.AdminMainEntity;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.AdminMainRepository;
import com.svc.ems.svc.auth.JwtAuthLoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class JwtAuthLoginServiceImpl implements JwtAuthLoginService {

    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthLoginServiceImpl.class);
    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AdminMainRepository adminMainRepository;
    private final MemberMainRepository memberRepository;

    public JwtAuthLoginServiceImpl(JwtUtil jwtUtil,
                                   JwtUserDetailsService userDetailsService,
                                   JwtMemberDetailsService memberDetailsService,
                                   PasswordEncoder passwordEncoder,
                                   AdminMainRepository userMainRepository, MemberMainRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.adminMainRepository = userMainRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public  ResponseEntity<ApiResponseTemplate<String>> authLogin(LoginRequest loginRequest, HttpServletResponse response) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        logger.info("login email: {}", email);
        // 確定身份類型（USER 或 MEMBER）
        boolean isUser = userDetailsService.userExists(email);
        boolean isMember = memberDetailsService.memberExists(email);

        if (!isUser && !isMember) {
            return ResponseEntity.ok(ApiResponseTemplate.fail(400,  "Invalid email or password."));
        }
        UserDetails userDetails;
        String type;
        String storedEncryptedPassword;
        try {
            if (isUser) {
                AdminMainEntity user = adminMainRepository.findByEmail(email).orElseThrow();
                userDetails = userDetailsService.loadUserByUsername(email);
                type = "USER";
                storedEncryptedPassword = user.getPassword(); // **取出加密後的密碼**
            } else {
                MemberMainEntity member = memberRepository.findByEmail(email).orElseThrow();
                userDetails = memberDetailsService.loadUserByUsername(email);
                type = "MEMBER";
                storedEncryptedPassword = member.getPassword(); // **取出加密後的密碼**
            }

            // **比對密碼解密**
            if (!passwordEncoder.matches(password, storedEncryptedPassword)) {
                ResponseEntity.ok(ApiResponseTemplate.fail(400,"Invalid email or password."));};
            // 取得登入 IP 與 User-Agent

        } catch (Exception e) {
            if (e.getMessage().equals("Not found with email")) {
              return ResponseEntity.ok(ApiResponseTemplate.fail(400,  "Invalid email or password."));
            } else if (e.getMessage().equals("Account is disabled")) {
                return ResponseEntity.ok(ApiResponseTemplate.fail(400, "Account is disabled."));
            } else {
                return ResponseEntity.ok(ApiResponseTemplate.fail(400, "Something going wrong."));
            }
        }


        // 驗證密碼
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            return ResponseEntity.ok(ApiResponseTemplate.fail(400, "Invalid email or password."));

        }

        // 生成 JWT

        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();
        String accessToken = jwtUtil.generateAccessToken(email, "MEMBER", roles);
        String refreshToken = jwtUtil.generateRefreshToken(email);

        // **存入 HttpOnly Cookie**
        Cookie accessCookie = new Cookie("AUTH_TOKEN", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(60 * 60 * 2); // 2 小時有效

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60);  // 7 天有效

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        // 6️⃣ 回應成功消息
        return ResponseEntity.ok(ApiResponseTemplate.success("member login success"));

    }


}