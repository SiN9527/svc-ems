package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtAdminDetailsService;
import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.*;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.entity.MemberMainRoleEntity;
import com.svc.ems.entity.MemberMainRolePkEntity;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.exception.ServiceException;
import com.svc.ems.repo.AdminMainRepository;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.MemberMainRoleRepository;
import com.svc.ems.svc.mail.EmailService;
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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberMainRepository memberMainRepository;
    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(MemberAuthServiceImpl.class);
    private final JwtUtil jwtUtil;
    private final JwtAdminDetailsService userDetailsService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    private final AdminMainRepository userRepository;

    private final MemberMainRoleRepository memberMainRoleRepository;
    private final MapperUtils mapperUtils;

    public MemberAuthServiceImpl(MemberMainRepository memberMainRepository, JwtUtil jwtUtil,
                                 JwtAdminDetailsService userDetailsService,
                                 JwtMemberDetailsService memberDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 EmailService emailService, AdminMainRepository userRepository, MemberMainRoleRepository memberMainRoleRepository, MapperUtils mapperUtils) {
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
    public ResponseEntity<ApiResponseTemplate<String>> memberRegister(@RequestBody MemberRegisterRequest req) {


        // 驗證 email 是否已存在
        if (memberMainRepository.existsByEmail(req.getEmail())) {
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(),
                    ErrorCode.EMAIL_ALREADY_REGISTERED
            ));
        }

        // 密碼格式驗證
        if (!isValidPassword(req.getPassword())) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(),
                    ErrorCode.PASSWORD_TOO_WEAK
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
    public ResponseEntity<ApiResponseTemplate<String>> verifyEmail(@RequestBody Map<String, String> tokenMap, HttpServletResponse response) {

       String token = tokenMap.get("token");
        // **解析 Token**
        String email = jwtUtil.extractUsername(token);
        if (email == null || jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.TOKEN_INVALID));
        }

        // **更新資料庫，標記使用者已驗證**
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElse(null);
        if (member == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, ErrorCode.MEMBER_NOT_FOUND));
        }

        member.setEnabled(true);
        memberMainRepository.save(member);


        // **生成 JWT 並存入 HttpOnly Cookie**
        String newtoken = generateAuthToken(email, response);

        return ResponseEntity.ok(ApiResponseTemplate.success("Email verified successfully! You are now logged in."));
    }


    // **取得會員資料**
    @Override
    public ResponseEntity<ApiResponseTemplate<MemberProfileResponse>> memberGetProfile(UserDetails userDetails) {

        String email = userDetails.getUsername(); // 直接從 Spring Security 取得 email

        // 從資料庫查詢用戶資訊
        MemberMainEntity member = memberMainRepository.findByEmail(email).orElse(null);

        if (member == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }

        // 建立回應對象
        MemberProfileResponse response = MapperUtils.map(member, MemberProfileResponse.class);
        response.setMemberType("MEMBER");
        return ResponseEntity.ok(ApiResponseTemplate.success("Member profile retrieved successfully.", response));
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberFindPwd(AdminLoginRequest req) {
        return null;
    }


    // **忘記密碼 API**
    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberForgotPwd(MemberPwdUpdateRequest req, UserDetails userDetails) {

        String email = userDetails.getUsername();
        //  檢查會員是否存在
        if (!memberMainRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }

        //  發送密碼重設 Email
        emailService.sendPasswordResetEmail(email);

        //  回應成功消息
        return ResponseEntity.ok(ApiResponseTemplate.success("reset password email sent successfully"));
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberResetPwd(@RequestBody MemberResetPwdRequest req, UserDetails userDetails) {


        String token = req.getToken();

        String newPassword = req.getPassword();

        //  檢查密碼格式
        if (!isValidPassword(newPassword)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.PASSWORD_TOO_WEAK));
        }


        // **解析 Token**
        String email = userDetails.getUsername();
//        if (email == null || jwtUtil.isTokenExpired(token)) {
//            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, "INVALID_OR_EXPIRED_TOKEN"));
//        }


        //  查找會員
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(ErrorCode.INVALID_VERIFICATION_URL));
        if (member == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
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
        // **清除 Cookie**
        // **清除 Cookie**
        Cookie accessCookie = new Cookie("AUTH_TOKEN", null);
        clearCookies(accessCookie);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", null);
        clearCookies(refreshCookie);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(ApiResponseTemplate.success("Logout successful"));
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdatePwd(MemberPwdUpdateRequest req, UserDetails userDetails,HttpServletResponse response) {


        Optional<MemberMainEntity> member = jwtUtil.validateAndGetEntity(userDetails, memberMainRepository);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }
        String newPassword = (passwordEncoder.encode(req.getNewPassword()));
        member.get().setPassword(newPassword);
        memberMainRepository.save(member.get());

        // **清除 Cookie**
        Cookie accessCookie = new Cookie("AUTH_TOKEN", null);
        clearCookies(accessCookie);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", null);
        clearCookies(refreshCookie);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(ApiResponseTemplate.success("Password updated successfully , please login again"));
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
        return accessToken;
    }

    private ResponseEntity<ApiResponseTemplate<String>> tokenValidation(String token) {

        String email = jwtUtil.extractUsername(token);

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.TOKEN_INVALID));
        }

        // **檢查 Token 是否過期**
        if (jwtUtil.isTokenExpired(token)) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.TOKEN_EXPIRED));
        }

        return null;
    }


    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberRefreshToken(
            @CookieValue(value = "REFRESH_TOKEN", required = false) String refreshToken,
            HttpServletResponse response) {

        if (refreshToken == null || !jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponseTemplate.fail(401, ErrorCode.INVALID_FRESH_TOKEN));
        }

        // **解析 Refresh Token 取得 Email**
        String email = jwtUtil.extractUsername(refreshToken);

        // **查詢用戶**
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(ErrorCode.MEMBER_NOT_FOUND));

        // **重新產生新的 Access Token**
        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();
        String newAccessToken = jwtUtil.generateAccessToken(email, "MEMBER", roles);

        // **更新 HttpOnly Cookie**
        Cookie cookie = new Cookie("AUTH_TOKEN", newAccessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);  // 1 小時過期
        response.addCookie(cookie);

        return ResponseEntity.ok(ApiResponseTemplate.success("Token refreshed successfully"));
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> memberUpdateProfile(MemberUpdateRequest req, UserDetails userDetails,HttpServletResponse response) {
        Optional<MemberMainEntity> member = jwtUtil.validateAndGetEntity(userDetails, memberMainRepository);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }

        req.setMemberId(member.get().getMemberId());
        req.setEmail(member.get().getEmail());
        req.setPassword(member.get().getPassword());


        MemberMainEntity entity = MapperUtils.map(req, MemberMainEntity.class);
        entity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setUpdatedBy(userDetails.getUsername());
        entity.setCreatedBy(member.get().getCreatedBy());
        entity.setCreatedAt(member.get().getCreatedAt());
        entity.setEnabled(member.get().getEnabled());
        entity.setRegistrationDate(member.get().getRegistrationDate());

        memberMainRepository.save(entity);

        // **清除 Cookie**
        Cookie accessCookie = new Cookie("AUTH_TOKEN", null);
        clearCookies(accessCookie);

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", null);
        clearCookies(refreshCookie);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(ApiResponseTemplate.success("Profile updated successfully "));
    }

    private void clearCookies(Cookie cookie) {
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
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