package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

// 3️⃣ 查詢會員詳細資料 Response
@Data
public class AdminMemberProfileResponse {

    @JsonProperty("MemberId")
    private String memberId;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("Organization")
    private String organization;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("Enabled")
    private Boolean enabled;

    @JsonProperty("Roles")
    private List<String> roles;
}