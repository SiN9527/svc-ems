package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberProfileCookie {

    @JsonProperty("Email")
    private String email;

    @JsonProperty("MemberType")
    private String memberType;

    @JsonProperty("MemberId")
    private String memberId;
}
