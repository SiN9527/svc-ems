package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberPwdRequest {


    /**
     * 信箱 (必填)
     * - 必須為有效的 Email 格式
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @JsonProperty("Email")
    private String email;

    /**
     * 密碼 (必填)
     * - 必須至少 8 碼，包含大小寫與數字
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @JsonProperty("OldPassword")
    private String oldPassword;

    /**
     * 密碼 (必填)
     * - 必須至少 8 碼，包含大小寫與數字
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @JsonProperty("NewPassword")
    private String newPassword;


}
