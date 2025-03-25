package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// 1️⃣ 查詢會員清單 Request
@Data
public class AdminMemberListRequest {

    @JsonProperty("EventId")
    private String eventId; // 活動 ID
}