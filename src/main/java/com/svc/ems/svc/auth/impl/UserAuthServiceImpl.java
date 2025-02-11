package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.auth.UserLoginResponse;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.entity.UserMain;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.svc.auth.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserMainRepository userRepository;

    public UserAuthServiceImpl(JwtUtil jwtUtil,
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
    public ResponseEntity<?> login(UserLoginRequest loginRequest) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println("email: " + email);
        log.info("email: {}", email);
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
        log.info("roles: {}", roles);
        log.info("type: {}", type);
        log.info("email: {}", email);

        String token = jwtUtil.generateToken(email, type, roles);
        log.info("email: {}", token);
        log.info("email: {}", new UserLoginResponse(token, roles));
        // 返回 JWT 和其他信息
        return ResponseEntity.ok(new UserLoginResponse(token, roles));
    }


    @Override
    public ResponseEntity<?> userRegister(UserRegisterRequest req) {
        UserMain user = new UserMain();
        user.setEmail(req.getEmail());
        user.setUserName(req.getUserName());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully.");
    }
}

// // **使用 Spring Security 的 `authenticate()` 驗證身份**  不手動loadUser
//    Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(email, password)
//    );
//
//    log.info("驗證通過: {}", authentication.getName());
//
//    // **取得 UserDetails**
//    UserDetails userDetails = (UserDetails) authentication.getPrincipal();