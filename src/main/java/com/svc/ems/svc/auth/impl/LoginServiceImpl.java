package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.LoginResponse;
import com.svc.ems.svc.auth.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;

    public LoginServiceImpl(JwtUtil jwtUtil,
                          JwtUserDetailsService userDetailsService,
                          JwtMemberDetailsService memberDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {



        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // 確定身份類型（USER 或 MEMBER）
        boolean isUser = userDetailsService.userExists(email);
        boolean isMember = memberDetailsService.memberExists(email);

        if (!isUser && !isMember) {
            return ResponseEntity.badRequest().body("Invalid email or password.");
        }

        UserDetails userDetails;
        String type;

        if (isUser) {
            userDetails = userDetailsService.loadUserByUsername(email);
            type = "USER"; // 後台使用者
        } else {
            userDetails = memberDetailsService.loadUserByUsername(email);
            type = "MEMBER"; // 會員
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

        // 返回 JWT 和其他信息
        return ResponseEntity.ok(new LoginResponse(token, roles));
    }
}
