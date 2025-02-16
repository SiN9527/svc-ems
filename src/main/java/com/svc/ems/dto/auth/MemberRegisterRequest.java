package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MemberRegisterRequest {


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
    @JsonProperty("Password")
    private String password;

    /**
     * 稱謂 (Dr., Mr., Ms., etc.)
     */
    @NotBlank(message = "Title cannot be empty")
    @JsonProperty("Title")
    private String title;

    /**
     * 名字
     */
    @NotBlank(message = "First name cannot be empty")
    @JsonProperty("FirstName")
    private String firstName;

    /**
     * 姓氏
     */
    @NotBlank(message = "Last name cannot be empty")
    @JsonProperty("LastName")
    private String lastName;

    /**
     * 性別 (必須是 Male, Female 或 Other)
     */
    @NotBlank(message = "Gender cannot be empty")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    @JsonProperty("Gender")
    private String gender;

    /**
     * 出生日期 (格式: YYYY-MM-DD)
     */
    @NotBlank(message = "Date of birth cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of Birth must be in YYYY-MM-DD format")
    @JsonProperty("DateOfBirth")
    private String dateOfBirth;

    /**
     * 部門名稱
     */
    @NotBlank(message = "Department cannot be empty")
    @JsonProperty("Department")
    private String department;

    /**
     * 隸屬組織
     */
    @NotBlank(message = "Affiliation organization cannot be empty")
    @JsonProperty("AffiliationOrganization")
    private String affiliationOrganization;

    /**
     * 所在城市
     */
    @NotBlank(message = "City of affiliation cannot be empty")
    @JsonProperty("CityOfAffiliation")
    private String cityOfAffiliation;

    /**
     * 所在國家
     */
    @NotBlank(message = "Country of affiliation cannot be empty")
    @JsonProperty("CountryOfAffiliation")
    private String countryOfAffiliation;

    /**
     * 電話號碼 (允許空白或 10~15 位數字)
     */
    @NotBlank(message = "Telephone number cannot be empty")
    @Pattern(regexp = "\\d{10,15}", message = "Telephone number must be 10-15 digits")
    @JsonProperty("TelNumber")
    private String telNumber;

    /**
     * 手機號碼 (允許空白或 10~15 位數字)
     */
    @NotBlank(message = "Mobile number cannot be empty")
    @Pattern(regexp = "\\d{10,15}", message = "Mobile number must be 10-15 digits")
    @JsonProperty("Mobile")
    private String mobile;

    /**
     * 註冊日期 (格式: YYYY-MM-DD HH:mm:ss)
     */
    @NotBlank(message = "Registration date cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Registration date must be in YYYY-MM-DD HH:mm:ss format")
    @JsonProperty("RegistrationDate")
    private String registrationDate;

    /**
     * 創建者
     */
    @NotBlank(message = "Created by cannot be empty")
    @JsonProperty("CreatedBy")
    private String createdBy;

    /**
     * 創建時間 (格式: YYYY-MM-DD HH:mm:ss)
     */
    @NotBlank(message = "Created at cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Created at must be in YYYY-MM-DD HH:mm:ss format")
    @JsonProperty("CreatedAt")
    private String createdAt;

    /**
     * 更新者
     */
    @NotBlank(message = "Updated by cannot be empty")
    @JsonProperty("UpdatedBy")
    private String updatedBy;

    /**
     * 更新時間 (格式: YYYY-MM-DD HH:mm:ss)
     */
    @NotBlank(message = "Updated at cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", message = "Updated at must be in YYYY-MM-DD HH:mm:ss format")
    @JsonProperty("UpdatedAt")
    private String updatedAt;

}
