package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.*;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.entity.MemberMainRoleEntity;
import com.svc.ems.entity.MemberMainRolePkEntity;
import com.svc.ems.exception.ServiceException;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.MemberMainRoleRepository;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.svc.auth.EmailService;
import com.svc.ems.svc.auth.MemberAuthService;
import com.svc.ems.utils.MapperUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    private final EmailService emailService;
    private final UserMainRepository userRepository;

    private final MemberMainRoleRepository memberMainRoleRepository;
    private final MapperUtils mapperUtils;

    public MemberAuthServiceImpl(MemberMainRepository memberMainRepository, JwtUtil jwtUtil,
                                 JwtUserDetailsService userDetailsService,
                                 JwtMemberDetailsService memberDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 EmailService emailService, UserMainRepository userRepository, MemberMainRoleRepository memberMainRoleRepository, MapperUtils mapperUtils) {
        this.memberMainRepository = memberMainRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.memberMainRoleRepository = memberMainRoleRepository;
        this.mapperUtils = mapperUtils;
    }


    /**
     * 註冊 API，建立新使用者後回傳統一格式的成功訊息。
     *
     * @param req 前端傳入的使用者註冊資料
     * @return 統一格式的 ApiResponse 物件，payload 為成功訊息
     */
    @PostMapping("/register")
    public  ResponseEntity<ApiResponseTemplate<String>> memberRegister(@RequestBody MemberRegisterRequest req) {


        // 驗證 email 是否已存在
        if (memberMainRepository.existsByEmail(req.getEmail())) {
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
            return   ResponseEntity.badRequest().body(ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(),
                    "Email already exists. Please use another email address."
            ));
        }

        // 密碼格式驗證
        if (!isValidPassword(req.getPassword())) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(),
                    "Password must contain upper/lower case letters, numbers, and special characters."
            ));


        }

        Timestamp timeNow = new Timestamp(System.currentTimeMillis());
        // 建立新使用者實體，並設定相關欄位
        MemberMainEntity entity = MapperUtils.map(req, MemberMainEntity.class);
        String uuid = UUID.randomUUID().toString();
        entity.setMemberId(uuid); // 產生隨機的 memberId
        entity.setPassword(passwordEncoder.encode(req.getPassword()));
        entity.setEnabled(false); // 預設帳號未啟用
        entity.setCreatedAt(timeNow); // 設定建立時間
        entity.setRegistrationDate(timeNow); // 設定註冊時間
        memberMainRepository.save(entity);


        MemberMainRolePkEntity pk = new MemberMainRolePkEntity(); // 建立主鍵
        pk.setMemberId(uuid); // uuid
        pk.setRoleId(2L); // role 的角色表

        MemberMainRoleEntity memberMainRole = new MemberMainRoleEntity();
        memberMainRole.setPk(pk);
        memberMainRole.setCreatedBy("SYS"); // 系統自動輸入
        memberMainRoleRepository.save(memberMainRole);

        emailService.sendVerificationEmail(entity.getEmail());
        // 使用 ApiResponse.success() 包裝成功訊息，再回傳 ResponseEntity
        return ResponseEntity.ok(ApiResponseTemplate.success("User registered successfully ! Please check your email to verify your account."));
    }

    @Override
    public  ResponseEntity<ApiResponseTemplate<String>> verifyEmail(String token, HttpServletResponse response) {


        // **解析 Token**
        String email = jwtUtil.extractUsername(token);
        if (email == null || jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, "INVALID_OR_EXPIRED_TOKEN"));
        }

        // **更新資料庫，標記使用者已驗證**
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElse(null);
        if (member == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, "MEMBER_NOT_FOUND"));
        }

        member.setEnabled(true);
        memberMainRepository.save(member);


        // **生成 JWT 並存入 HttpOnly Cookie**
        generateAuthToken(email, response);

        return ResponseEntity.ok(ApiResponseTemplate.success("Email verified successfully! You are now logged in."));
    }


    // **取得會員資料**
    @Override
    public  ResponseEntity<ApiResponseTemplate<MemberProfileResponse>> memberGetProfile(@CookieValue(value = "AUTH_TOKEN", required = false) String token) {
        if (token == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(401, "TOKEN_MISSING"));
        }

        // **解析 Token**
        String email = jwtUtil.extractUsername(token);
        if (email == null || jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, "INVALID_OR_EXPIRED_TOKEN"));
        }


        // 從資料庫查詢用戶資訊
        MemberMainEntity member = memberMainRepository.findByEmail(email).orElse(null);

        if (member == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, "MEMBER_NOT_FOUND"));
        }

        // 建立回應對象
        MemberProfileResponse response = MapperUtils.map(member, MemberProfileResponse.class);
        response.setMemberType("MEMBER");
       return ResponseEntity.ok(ApiResponseTemplate.success("Member profile retrieved successfully.", response));
    }

    @Override
    public  ResponseEntity<ApiResponseTemplate<?>> memberFindPwd(UserLoginRequest req) {
        return null;
    }


    // **忘記密碼 API**
    @Override
    public  ResponseEntity<ApiResponseTemplate<?>> memberForgotPwd(MemberPwdUpdateRequest req) {

        String email = req.getEmail();
        //  檢查會員是否存在
        if (!memberMainRepository.existsByEmail(email)) {
          return  ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, "MEMBER_NOT_FOUND"));
        }

        //  發送密碼重設 Email
        emailService.sendPasswordResetEmail(email);

        //  回應成功消息
        return ResponseEntity.ok(ApiResponseTemplate.success("reset password email sent successfully"));
    }

    @Override
    public  ResponseEntity<ApiResponseTemplate<?>> memberResetPwd(@RequestBody MemberResetPwdRequest req) {

        String token = req.getToken();

        String newPassword = req.getPassword();

        //  檢查密碼格式
        if (!isValidPassword(newPassword)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, "INVALID_PASSWORD"));
        }


        // **解析 Token**
        String email = jwtUtil.extractUsername(token);
        if (email == null || jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, "INVALID_OR_EXPIRED_TOKEN"));
        }


        //  查找會員
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException("無效的驗證連結"));
        if (member == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, "MEMBER_NOT_FOUND"));
        }

        String encryptedPassword = passwordEncoder.encode(newPassword);

        //  更新密碼（加密後存入）
        member.setPassword(passwordEncoder.encode(encryptedPassword));
        memberMainRepository.save(member);

        //  回應成功消息
        return ResponseEntity.ok(ApiResponseTemplate.success("Password reset successfully"));
    }

    /**
     * 會員登出 API
     */
    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberLogout(HttpServletResponse response) {
        Cookie authCookie = new Cookie("AUTH_TOKEN", null);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(true);
        authCookie.setPath("/");
        authCookie.setMaxAge(0); // 立即失效
        response.addCookie(authCookie);

        return ResponseEntity.ok(ApiResponseTemplate.success("登出成功"));
    }
    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(@CookieValue(value = "AUTH_TOKEN", required = false) MemberPwdUpdateRequest req) {


        Optional<MemberMainEntity> member = jwtUtil.validateAndGetEntity(req.getToken(), memberMainRepository);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, "MEMBER_NOT_FOUND"));
        }
        String newPassword = (passwordEncoder.encode(req.getNewPassword()));
        member.get().setPassword(newPassword);
        memberMainRepository.save(member.get());
        return ResponseEntity.ok(ApiResponseTemplate.success("Password updated successfully"));
    }


    // 密碼格式驗證
    private boolean isValidPassword(String password) {
        // 密碼必須包含大寫字母、小寫字母、數字，且長度至少 8 位
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

    /**
     * 產生 JWT 並存入 HttpOnly Cookie
     */
    private String generateAuthToken(String email, HttpServletResponse response) {

        // 生成 JWT
        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);

        String type = "MEMBER";
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();
        String jwtToken = jwtUtil.generateToken(email, type, roles);

        Cookie cookie = new Cookie("AUTH_TOKEN", jwtToken);
        cookie.setHttpOnly(true); // 無法透過 JavaScript 存取
        cookie.setSecure(true); // 只允許 HTTPS
        cookie.setPath("/"); // 全域有效
        cookie.setMaxAge(3600); // 1 小時過期
        response.addCookie(cookie); // 加入 Cookie
        return jwtToken;
    }

    private ResponseEntity<ApiResponseTemplate<String>> tokenValidation(String token) {

        String email = jwtUtil.extractUsername(token);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, "INVALID_TOKEN"));
        }

        // **檢查 Token 是否過期**
        if (jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, "TOKEN_EXPIRED"));
        }

        return null;
    }


    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberRefreshToken() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdateProfile() {
        return null;
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