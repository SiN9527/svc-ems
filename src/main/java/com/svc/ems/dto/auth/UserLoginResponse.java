package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponse {

    @JsonProperty("Token")
    private String token; // JWT Token

    @JsonProperty("Roles")
    private List<String> roles; // 用戶的角色清單

    @JsonProperty("UserName")
    private String userName; // 用戶名或 email

    @JsonProperty("UserId")
    private Long userId; // 用戶 ID (可選)

    @JsonProperty("MemberId")
    private String memberId; // 會員 ID (可選)

    public UserLoginResponse(String token, List<String> roles) {
        this.token = token;
        this.roles = roles;
    }
}
