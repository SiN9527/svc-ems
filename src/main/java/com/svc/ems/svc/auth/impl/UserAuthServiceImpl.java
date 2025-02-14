package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.LoggingAspect;
import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.auth.UserLoginResponse;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.UserMain;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.svc.auth.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Service
public class UserAuthServiceImpl implements UserAuthService {

    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);
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

        logger.info("login email: {}", email);
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
        log.info("email: {}", token);
        logger.info("UserLoginResponse: {}", new UserLoginResponse(token, roles));
        // 返回 JWT 和其他信息
        // 使用 ApiResponse.success() 包裝成功訊息與資料，再回傳 ResponseEntity
        return ResponseEntity.ok(ApiResponseTemplate.success(new UserLoginResponse(token, roles)));

    }


    /**
     * 註冊 API，建立新使用者後回傳統一格式的成功訊息。
     *
     * @param req 前端傳入的使用者註冊資料
     * @return 統一格式的 ApiResponse 物件，payload 為成功訊息
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponseTemplate<String>> userRegister(@RequestBody UserRegisterRequest req) {



        if (userRepository.existsByEmail(req.getEmail())) {

            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
            return ResponseEntity.ok(ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(), "Registration failed",
                    "Email already exists. Please use another email address."
            ));

        }
        // 建立新使用者實體，並設定相關欄位
        UserMain user = new UserMain();
        user.setEmail(req.getEmail());
        user.setUserName(req.getUserName());
        user.setEnabled(false); // 預設帳號未啟用
        // 密碼加密處理
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        // 儲存使用者資料到資料庫
        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());
        // 使用 ApiResponse.success() 包裝成功訊息，再回傳 ResponseEntity
        return ResponseEntity.ok(ApiResponseTemplate.success("User registered successfully."));
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