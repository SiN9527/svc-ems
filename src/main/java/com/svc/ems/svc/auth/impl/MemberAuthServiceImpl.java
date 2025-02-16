package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.MemberRegisterRequest;
import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.MemberMain;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.svc.auth.MemberAuthService;
import com.svc.ems.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;

@Slf4j
@Service
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberMainRepository memberMainRepository;
    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(MemberAuthServiceImpl.class);
    private final JwtUtil jwtUtil;
    private final JwtUserDetailsService userDetailsService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserMainRepository userRepository;

    public MemberAuthServiceImpl(MemberMainRepository memberMainRepository, JwtUtil jwtUtil,
                                 JwtUserDetailsService userDetailsService,
                                 JwtMemberDetailsService memberDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 UserMainRepository userRepository) {
        this.memberMainRepository = memberMainRepository;
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
    @PostMapping("/register")
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(@RequestBody MemberRegisterRequest req) {


        // 驗證 email 是否已存在
        if (userRepository.existsByEmail(req.getEmail())) {
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
            return ResponseEntity.ok(ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(), "Registration failed",
                    "Email already exists. Please use another email address."
            ));
        }

        // 密碼格式驗證
        if (!isValidPassword(req.getPassword())) {
            throw new IllegalArgumentException("Password must contain upper/lower case letters, numbers, and special characters.");
        }

        Timestamp timeNow = new Timestamp(System.currentTimeMillis());
        // 建立新使用者實體，並設定相關欄位
        MemberMain entity = MapperUtils.map(req, MemberMain.class);
        entity.setEnabled(false); // 預設帳號未啟用
        entity.setCreatedAt(timeNow); // 設定建立時間
        memberMainRepository.save(entity);

        // 使用 ApiResponse.success() 包裝成功訊息，再回傳 ResponseEntity
        return ResponseEntity.ok(ApiResponseTemplate.success("User registered successfully."));
    }


    @Override
    public ResponseEntity<?> memberFindPwd(UserLoginRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<?> memberLogout() {
        return null;
    }

    @Override
    public ResponseEntity<?> memberRefreshToken() {
        return null;
    }

    @Override
    public ResponseEntity<?> memberGetProfile() {
        return null;
    }

    @Override
    public ResponseEntity<?> memberUpdateProfile() {
        return null;
    }

    @Override
    public ResponseEntity<?> memberUpdatePwd() {
        return null;
    }



    // 密碼格式驗證
    private boolean isValidPassword(String password) {
      // 密碼必須包含大寫字母、小寫字母、數字，且長度至少 8 位
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$");
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