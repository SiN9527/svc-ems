package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminRegisterRequest {


    @JsonProperty("Email")
    private String email;

    @NotBlank
    @JsonProperty("Account")
    private String account;

    @NotBlank
    @JsonProperty("Password")
    private String password;

    @NotBlank
    @JsonProperty("UserName")
    private String userName;
}
