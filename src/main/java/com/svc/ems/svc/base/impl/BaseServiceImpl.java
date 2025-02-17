package com.svc.ems.svc.base.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.SwaggerUserLoginRequest;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.svc.base.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BaseServiceImpl implements BaseService {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserMainRepository userRepository;

    public BaseServiceImpl(JwtUtil jwtUtil,
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
    public String getToken(SwaggerUserLoginRequest req) {

        String email = req.getEmail();
        String password = req.getPassword();

        // 確定身份類型（USER 或 MEMBER）
        boolean isUser = userDetailsService.userExists(email);
        boolean isMember = memberDetailsService.memberExists(email);

        if (!isUser && !isMember) {
            return "Invalid email or password.";
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
            return "Invalid email or password.";
        }

        // 生成 JWT
        List<String> roles = new ArrayList<>();

        return jwtUtil.generateToken(email, type, roles);

    }
}
