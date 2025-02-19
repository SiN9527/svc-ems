package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.UserLoginResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.entity.UserMainEntity;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.svc.auth.JwtAuthLoginService;
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
    private final UserMainRepository userMainRepository;
    private final MemberMainRepository memberRepository;

    public JwtAuthLoginServiceImpl(JwtUtil jwtUtil,
                                   JwtUserDetailsService userDetailsService,
                                   JwtMemberDetailsService memberDetailsService,
                                   PasswordEncoder passwordEncoder,
                                   UserMainRepository userMainRepository, MemberMainRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userMainRepository = userMainRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public ApiResponseTemplate<UserLoginResponse> authLogin(LoginRequest loginRequest) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        logger.info("login email: {}", email);
        // 確定身份類型（USER 或 MEMBER）
        boolean isUser = userDetailsService.userExists(email);
        boolean isMember = memberDetailsService.memberExists(email);

        if (!isUser && !isMember) {
            return ApiResponseTemplate.fail(400, "login error.", "Invalid email or password.");
        }
        UserDetails userDetails;
        String type;
        String storedEncryptedPassword;
        try {
            if (isUser) {
                UserMainEntity user = userMainRepository.findByEmail(email).orElseThrow();
                userDetails = userDetailsService.loadUserByUsername(email);
                type = "USER";
                storedEncryptedPassword = user.getPassword(); // **取出加密後的密碼**
            } else {
                MemberMainEntity member = memberRepository.findByEmail(email).orElseThrow();
                userDetails = memberDetailsService.loadUserByUsername(email);
                type = "MEMBER";
                storedEncryptedPassword = member.getPassword(); // **取出加密後的密碼**
            }

            // **比對密碼，不需要解密**
            if (!passwordEncoder.matches(password, storedEncryptedPassword)) {
                ResponseEntity.ok(ApiResponseTemplate.fail(400, "Invalid password.", "Invalid email or password."));};
            // 取得登入 IP 與 User-Agent

        } catch (Exception e) {
            if (e.getMessage().equals("Not found with email")) {
                return ApiResponseTemplate.fail(400, "login error.", " Email not found.");
            } else if (e.getMessage().equals("Account is disabled")) {
                return ApiResponseTemplate.fail(400, "login error.", "Account is disabled.");
            } else {
                return ApiResponseTemplate.fail(400, "runtime error.", "Please connect IT.");
            }
        }


        // 驗證密碼
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            return ApiResponseTemplate.fail(400, "login error.", "Invalid email or password.");

        }

        // 生成 JWT
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();


        String token = jwtUtil.generateToken(email, type, roles);
        log.info("email: {}", token);
        logger.info("UserLoginResponse: {}", new UserLoginResponse(token, roles));
        // 返回 JWT 和其他信息
        // 使用 ApiResponse.success() 包裝成功訊息與資料，再回傳 ResponseEntity

        // 4️⃣ 生成 Email 驗證 Token
        String verificationToken = jwtUtil.generateVerificationToken(email);

        // 5️⃣ 發送驗證郵件
        //emailService.sendVerificationEmail(req.getEmail(), verificationToken);

        // 6️⃣ 回應成功消息
        return ApiResponseTemplate.success("會員登入成功 ",new UserLoginResponse(token, roles));

    }


}