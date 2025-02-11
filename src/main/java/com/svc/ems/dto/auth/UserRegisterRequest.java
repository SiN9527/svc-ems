package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotBlank
    @JsonProperty("Email")
    private String email;

    @NotBlank
    @JsonProperty("Password")
    private String password;

    @NotBlank
    @JsonProperty("UserName")
    private String userName;
}
