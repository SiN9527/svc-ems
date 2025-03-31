package com.svc.ems.svc.auth.impl;

import com.svc.ems.config.jwt.JwtAdminDetailsService;
import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.AdminMemberListRequest;
import com.svc.ems.dto.auth.AdminMemberProfileResponse;
import com.svc.ems.dto.auth.AdminRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.AdminEventEntity;
import com.svc.ems.entity.AdminMainEntity;
import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.repo.AdminEventRepository;
import com.svc.ems.repo.AdminMainRepository;
import com.svc.ems.repo.MemberEventRepository;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.svc.auth.AdminAuthService;
import com.svc.ems.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(AdminAuthServiceImpl.class);
    private final JwtUtil jwtUtil;
    private final JwtAdminDetailsService userDetailsService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AdminMainRepository adminMainRepository;
//    private final AdminMainRepository adminMainRepository;
   private final AdminEventRepository adminEventRepository;
//    private final EventRepository eventRepository;
    private final MemberMainRepository memberMainRepository;
    private final MemberEventRepository memberEventRepository;



    /**
     * 註冊 API，建立新使用者後回傳統一格式的成功訊息。
     *
     * @param req 前端傳入的使用者註冊資料
     * @return 統一格式的 ApiResponse 物件，payload 為成功訊息
     */

    public ResponseEntity<ApiResponseTemplate<String>> adminRegister(@RequestBody AdminRegisterRequest req) {


        if (adminMainRepository.existsByAccount(req.getAccount())) {
            log.info("Admin registration failed: Account already exists. Please use another Account.");
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
           return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400,
                   ErrorCode.ACCOUNT_ALREADY_REGISTERED)
            );

        }
        // 建立新使用者實體，並設定相關欄位
        AdminMainEntity user = new AdminMainEntity();
        user.setAccount(req.getAccount());
        user.setEmail(req.getEmail()!=null?req.getEmail():"");
        user.setUserName(req.getUserName());
        user.setEnabled(false); // 預設帳號未啟用
        // 密碼加密處理
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEventId("All");
        // 儲存使用者資料到資料庫
        adminMainRepository.save(user);
        logger.info("Admin registered successfully: {}", user.getEmail());
        // 使用 ApiResponse.success() 包裝成功訊息，再回傳 ResponseEntity
        return ResponseEntity.ok(ApiResponseTemplate.success("Admin registered successfully."));
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminLogin(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminLogout() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminRefreshToken(String refreshToken) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateProfile(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public  ResponseEntity<ApiResponseTemplate<List<AdminMemberProfileResponse>>> adminGetMemberList(AdminMemberListRequest req) {

        String account = SecurityContextHolder.getContext().getAuthentication().getName();
log.info("%%%%%%%%%%%%%55"+account);
        // 驗證管理員是否有對應活動的權限
        Optional<AdminEventEntity> adminEventOpt = adminEventRepository.findByAccountAndEventId(account, req.getEventId());

        if (adminEventOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseTemplate.fail(403, "You do not have access to this event."));
        }
        // 查詢該活動下所有的會員 ID
        List<String> memberIds = memberEventRepository.findMemberIdsByEventId(req.getEventId());

        // 查詢會員主資料
        List<MemberMainEntity> members = memberMainRepository.findAllByMemberIdIn(memberIds);

        // 封裝成 DTO 回傳
        List<AdminMemberProfileResponse> responseList = members.stream()
                .map(member -> MapperUtils.map(member, AdminMemberProfileResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseTemplate.success("Successfully retrieved member list.", responseList));


    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminGetMemberProfile(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateMemberProfile(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminDeleteMemberProfile(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminUpdatePwd(AdminRegisterRequest req) {
        return null;
    }


//    /**
//     * 取得該 Admin 所管理的活動下的所有會員清單
//     */
//    @Override
//    public ResponseEntity<ApiResponseTemplate<List<AdminMemberListResponse>>> adminGetMemberList(String adminId, String eventId) {
//
//        // 驗證該 admin 是否有管理該活動
//        boolean isAuthorized = adminMainEventRepository.existsByAdminIdAndEventId(adminId, eventId);
//        if (!isAuthorized) {
//            return ResponseEntity.status(403).body(ApiResponseTemplate.fail(403, "FORBIDDEN", "您無權限存取此活動資料"));
//        }
//
//        // 找出該活動的所有會員
//        List<MemberMainEntity> members = memberMainEventRepository.findAllByEventId(eventId)
//                .stream()
//                .map(MemberMainEventEntity::getMember)
//                .collect(Collectors.toList());
//
//        List<AdminMemberListResponse> responseList = members.stream()
//                .map(member -> MapperUtils.map(member, AdminMemberListResponse.class))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(ApiResponseTemplate.success("取得會員清單成功", responseList));
//    }
//
//    /**
//     * 根據 memberId 取得會員詳細資料
//     */
//    @Override
//    public ResponseEntity<ApiResponseTemplate<AdminMemberProfileResponse>> adminGetMemberProfile(String adminId, String eventId, String memberId) {
//
//        boolean isAuthorized = adminMainEventRepository.existsByAdminIdAndEventId(adminId, eventId);
//        if (!isAuthorized) {
//            return ResponseEntity.status(403).body(ApiResponseTemplate.fail(403, "FORBIDDEN", "無權限檢視會員資料"));
//        }
//
//        MemberMainEntity member = memberMainRepository.findById(memberId).orElse(null);
//        if (member == null) {
//            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, "MEMBER_NOT_FOUND", "找不到會員"));
//        }
//
//        AdminMemberProfileResponse response = MapperUtils.map(member, AdminMemberProfileResponse.class);
//        return ResponseEntity.ok(ApiResponseTemplate.success("取得會員資料成功", response));
//    }
//
//    /**
//     * 編輯會員資訊
//     */
//    @Override
//    @Transactional
//    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateMemberProfile(String adminId, String eventId, AdminMemberUpdateRequest req) {
//
//        boolean isAuthorized = adminMainEventRepository.existsByAdminIdAndEventId(adminId, eventId);
//        if (!isAuthorized) {
//            return ResponseEntity.status(403).body(ApiResponseTemplate.fail(403, "FORBIDDEN", "無權限編輯會員"));
//        }
//
//        MemberMainEntity member = memberMainRepository.findById(req.getMemberId()).orElse(null);
//        if (member == null) {
//            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, "MEMBER_NOT_FOUND", "找不到會員"));
//        }
//
//        // 更新欄位
//        member.setUserName(req.getUserName());
//        member.setEmail(req.getEmail());
//        member.setTitle(req.getTitle());
//        member.setPhone(req.getPhone());
//        memberMainRepository.save(member);
//
//        return ResponseEntity.ok(ApiResponseTemplate.success("會員資料更新成功"));
//    }
//
//    /**
//     * 刪除會員
//     */
//    @Override
//    @Transactional
//    public ResponseEntity<ApiResponseTemplate<?>> adminDeleteMemberProfile(String adminId, String eventId, String memberId) {
//
//        boolean isAuthorized = adminMainEventRepository.existsByAdminIdAndEventId(adminId, eventId);
//        if (!isAuthorized) {
//            return ResponseEntity.status(403).body(ApiResponseTemplate.fail(403, "FORBIDDEN", "無權限刪除會員"));
//        }
//
//        if (!memberMainRepository.existsById(memberId)) {
//            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, "MEMBER_NOT_FOUND", "找不到會員"));
//        }
//
//        // 刪除關聯表再刪主資料
//        memberMainEventRepository.deleteByMemberIdAndEventId(memberId, eventId);
//        memberMainRepository.deleteById(memberId);
//
//        return ResponseEntity.ok(ApiResponseTemplate.success("會員資料刪除成功"));
//    }
//
//
//
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminLogin(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminLogout() {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminRefreshToken(String refreshToken) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateProfile(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminGetMemberList(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminGetMemberProfile(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateMemberProfile(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminDeleteMemberProfile(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminUpdatePwd(AdminRegisterRequest req) {
//        return null;
//    }


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