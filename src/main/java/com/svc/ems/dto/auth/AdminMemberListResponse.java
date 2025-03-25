package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AdminMemberListResponse {

    @JsonProperty("MemberId")
    private String memberId;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("Role")
    private String role;

    @JsonProperty("Enabled")
    private Boolean enabled;
}