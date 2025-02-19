package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.UserMainEntity;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.svc.auth.UserAuthService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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


    /**
     * 註冊 API，建立新使用者後回傳統一格式的成功訊息。
     *
     * @param req 前端傳入的使用者註冊資料
     * @return 統一格式的 ApiResponse 物件，payload 為成功訊息
     */

    public ApiResponseTemplate<String> userRegister(@RequestBody UserRegisterRequest req) {


        if (userRepository.existsByEmail(req.getEmail())) {
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
            return ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(), "Registration failed",
                    "Email already exists. Please use another email address."
            );

        }
        // 建立新使用者實體，並設定相關欄位
        UserMainEntity user = new UserMainEntity();
        user.setEmail(req.getEmail());
        user.setUserName(req.getUserName());
        user.setEnabled(false); // 預設帳號未啟用
        // 密碼加密處理
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        // 儲存使用者資料到資料庫
        userRepository.save(user);
        logger.info("User registered successfully: {}", user.getEmail());
        // 使用 ApiResponse.success() 包裝成功訊息，再回傳 ResponseEntity
        return ApiResponseTemplate.success("User registered successfully.");
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