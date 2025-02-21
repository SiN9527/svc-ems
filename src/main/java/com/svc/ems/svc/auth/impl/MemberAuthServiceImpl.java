package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUserDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.MemberRegisterRequest;
import com.svc.ems.dto.auth.UserLoginRequest;
import com.svc.ems.dto.auth.VerifyRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.entity.MemberMainRoleEntity;
import com.svc.ems.entity.MemberMainRolePkEntity;
import com.svc.ems.entity.UserMainEntity;
import com.svc.ems.exception.ServiceException;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.MemberMainRoleRepository;
import com.svc.ems.repo.UserMainRepository;
import com.svc.ems.svc.auth.EmailService;
import com.svc.ems.svc.auth.MemberAuthService;
import com.svc.ems.utils.MapperUtils;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
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

    public MemberAuthServiceImpl(MemberMainRepository memberMainRepository, JwtUtil jwtUtil,
                                 JwtUserDetailsService userDetailsService,
                                 JwtMemberDetailsService memberDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 EmailService emailService, UserMainRepository userRepository, MemberMainRoleRepository memberMainRoleRepository) {
        this.memberMainRepository = memberMainRepository;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.memberMainRoleRepository = memberMainRoleRepository;
    }


    /**
     * 註冊 API，建立新使用者後回傳統一格式的成功訊息。
     *
     * @param req 前端傳入的使用者註冊資料
     * @return 統一格式的 ApiResponse 物件，payload 為成功訊息
     */
    @PostMapping("/register")
    public ApiResponseTemplate<String> memberRegister(@RequestBody MemberRegisterRequest req) {


        // 驗證 email 是否已存在
        if (memberMainRepository.existsByEmail(req.getEmail())) {
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
            return ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(), "Registration failed",
                    "Email already exists. Please use another email address."
            );
        }

        // 密碼格式驗證
        if (!isValidPassword(req.getPassword())) {
            return ApiResponseTemplate.fail(HttpStatus.BAD_REQUEST.value(), "Registration failed",
                    "Password must contain upper/lower case letters, numbers, and special characters.");
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
        return ApiResponseTemplate.success("會員註冊成功！請查收您的 Email 完成驗證.");
    }

    @Override
    public ApiResponseTemplate<String> verifyEmail(VerifyRequest req) {
        String token = req.getToken();

        // **解析 Token**
        String email;
        try {
            email = jwtUtil.extractUsername(token); // **從 Token 取得 Email**
        } catch (Exception e) {
            return ApiResponseTemplate.fail(400, "TOKEN_INVALID", "驗證失敗，無效的 Token");
        }

        // **檢查 Token 是否過期**
        if (jwtUtil.isTokenExpired(token)) {
            return ApiResponseTemplate.fail(400, "TOKEN_EXPIRED", "驗證失敗，Token 已過期");
        }

        // **更新資料庫，標記使用者已驗證**
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElse(null);
        if (member == null) {
            return ApiResponseTemplate.fail(404, "USER_NOT_FOUND", "找不到此使用者");
        }

        member.setEnabled(true);
        memberMainRepository.save(member);
        // **自動登入：發送 JWT Token**
        // 生成 JWT

        UserDetails userDetails = memberDetailsService.loadUserByUsername(email);
       String type = "MEMBER";
        List<String> roles = userDetails.getAuthorities().stream()
                .map(auth -> auth.getAuthority().replace("ROLE_", ""))
                .toList();
        String jwtToken = jwtUtil.generateToken(email, "type", roles);

        return ApiResponseTemplate.success("Validation Success , Auto Login", jwtToken);
    }

    @Override
    public ApiResponseTemplate<?> memberFindPwd(UserLoginRequest req) {
        return null;
    }


    @Override
    public ApiResponseTemplate<?> memberForgotPwd(String email) {
        //  檢查會員是否存在
        if (!memberMainRepository.existsByEmail(email)) {
            return ApiResponseTemplate.fail(404, "EMAIL_NOT_FOUND", "該 Email 並未註冊");
        }

        //  發送密碼重設 Email
        emailService.sendPasswordResetEmail(email);

        //  回應成功消息
        return ApiResponseTemplate.success("密碼重設信件已發送，請檢查您的 Email");
    }

    @Override
    public ApiResponseTemplate<?> memberResetPwd(String token, String password) {
        //  解析 Token 取得 Email
        String email = jwtUtil.extractEmail(token);

        //  查找會員
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException("無效的驗證連結"));

        //  更新密碼（加密後存入）
        member.setPassword(passwordEncoder.encode(password));
        memberMainRepository.save(member);

        //  回應成功消息
        return ApiResponseTemplate.success("密碼重設成功，請使用新密碼登入！");
    }

    @Override
    public ApiResponseTemplate<?> memberLogout() {
        return null;
    }

    @Override
    public ApiResponseTemplate<?> memberRefreshToken() {
        return null;
    }

    @Override
    public ApiResponseTemplate<?> memberGetProfile() {
        return null;
    }

    @Override
    public ApiResponseTemplate<?> memberUpdateProfile() {
        return null;
    }

    @Override
    public ApiResponseTemplate<?> memberUpdatePwd() {
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