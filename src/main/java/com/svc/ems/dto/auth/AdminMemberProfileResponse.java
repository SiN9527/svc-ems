package com.svc.ems.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

// 3️⃣ 查詢會員詳細資料 Response
@Data
public class AdminMemberProfileResponse {

    @JsonProperty("MemberType")
    private String memberType;

    @JsonProperty("MemberId")
    private String memberId;

    /**
     * 信箱 (必填)
     * - 必須為有效的 Email 格式
     */
    @NotBlank(message = "Email cannot be empty")
    @JsonProperty("Email")
    private String email;


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
    @JsonProperty("Gender")
    private String gender;

    /**
     * 出生日期 (格式: YYYY-MM-DD)
     */
    @NotBlank(message = "Date of birth cannot be empty")
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

    @JsonProperty("TelNumber")
    private String telNumber;

    /**
     * 手機號碼 (允許空白或 10~15 位數字)
     */
    @NotBlank(message = "Mobile number cannot be empty")
    @JsonProperty("Mobile")
    private String mobile;
    @JsonProperty("Enabled")
    private Boolean enabled;


}