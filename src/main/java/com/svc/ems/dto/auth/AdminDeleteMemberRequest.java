package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

// 5️⃣ 刪除會員 Request
@Data
public class AdminDeleteMemberRequest {

    @JsonProperty("MemberId")
    private String memberId;

    @JsonProperty("EventId")
    private String eventId;
}