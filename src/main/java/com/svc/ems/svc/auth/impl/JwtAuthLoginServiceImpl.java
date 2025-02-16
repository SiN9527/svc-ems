package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.UserLoginResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
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
    private final UserMainRepository userRepository;

    public JwtAuthLoginServiceImpl(JwtUtil jwtUtil,
                                   JwtUserDetailsService userDetailsService,
                                   JwtMemberDetailsService memberDetailsService,
                                   PasswordEncoder passwordEncoder,
                                   UserMainRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> authLogin(LoginRequest loginRequest) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        logger.info("login email: {}", email);
        // 確定身份類型（USER 或 MEMBER）
        boolean isUser = userDetailsService.userExists(email, password);
        boolean isMember = memberDetailsService.memberExists(email, password);

        if (!isUser && !isMember) {
            return ResponseEntity.badRequest().body("Invalid email or password.");
        }
        UserDetails userDetails;
        String type;

        try {
            if (isUser) {
                userDetails = userDetailsService.loadUserByUsername(email);
                type = "USER"; // 後台使用者
            } else {
                userDetails = memberDetailsService.loadUserByUsername(email);
                type = "MEMBER"; // 會員
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Not found with email")) {
                return ResponseEntity.badRequest().body("Not found with email");
            } else if (e.getMessage().equals("Account is disabled")) {
                return ResponseEntity.badRequest().body("Account is disabled");
            } else {
                return ResponseEntity.badRequest().body("something error.");
            }
        }


        // 驗證密碼
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid email or password.");
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
        return ResponseEntity.ok(ApiResponseTemplate.success(new UserLoginResponse(token, roles)));

    }


}