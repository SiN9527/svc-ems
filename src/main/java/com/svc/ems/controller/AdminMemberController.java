package com.svc.ems.controller;

import com.svc.ems.dto.auth.AdminMemberListRequest;
import com.svc.ems.dto.auth.AdminMemberProfileResponse;
import com.svc.ems.dto.auth.AdminRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin 功能：管理活動下的會員
 */
@RestController
@RequestMapping("/api/admin/member")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminAuthService adminAuthService;


    @PostMapping("/memberList")
    public ResponseEntity<ApiResponseTemplate<List<AdminMemberProfileResponse>>> adminGetMemberList(@RequestBody AdminMemberListRequest req) {
        return adminAuthService.adminGetMemberList(req);
    }
//
//    /**
//     * 查看某一位會員詳細資料
//     * @param eventId 活動 ID
//     * @param memberId 會員 ID
//     */
//    @PostMapping("/profile")
//    public ResponseEntity<ApiResponseTemplate<AdminMemberProfileResponse>> adminGetMemberProfile(@RequestParam String eventId,
//                                                                                                 @RequestParam String memberId) {
//        return adminAuthService.getMemberProfile(eventId, memberId);
//    }
//
//    /**
//     * 編輯會員資料
//     * @param req 編輯資料，包含 eventId、memberId 等
//     */
//    @PostMapping("/update")
//    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateMemberProfile(@RequestBody AdminMemberUpdateRequest req) {
//        return adminAuthService.updateMemberProfile(req);
//    }
//
//    /**
//     * 刪除會員資料
//     * @param eventId 活動 ID
//     * @param memberId 會員 ID
//     */
//    @PostMapping("/delete")
//    public ResponseEntity<ApiResponseTemplate<?>> adminDeleteMemberProfile(@RequestParam String eventId,
//                                                                           @RequestParam String memberId) {
//        return adminAuthService.deleteMemberProfile(eventId, memberId);
//    }

}