package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

// 1️⃣ 查詢會員清單 Request
@Data
public class AdminMemberListRequest {

//    @JsonProperty("Roles")
//    private List<String> roles; // 用戶的角色清單
//
//    @JsonProperty("UserName")
//    private String userName; // 用戶名或 email
//
//    @JsonProperty("AdminId")
//    private Long adminId; // 用戶 ID (可選)

    @JsonProperty("EventId")
    private String eventId; // 活動 ID (可選)

    @JsonProperty("EventName")
    private String eventName; // 活動名稱 (可選)
}