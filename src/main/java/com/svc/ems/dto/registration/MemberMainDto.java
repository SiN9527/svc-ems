package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class MemberMainDto {

    @JsonProperty("eventId")
    private String EventId;  // 活動ID

    @JsonProperty("title")
    private String Title; // 稱謂 Prof / Dr / Mr / Ms

    @JsonProperty("firstName")
    private String FirstName;  // 名

    @JsonProperty("LastName")
    private String lastName;  // 姓

    @JsonProperty("FullNameCn")
    private String fullNameCh;  // 中文姓名 (台灣/港澳必填)

    @JsonProperty("Gender")
    private String gender;  // 性別 MALE / FEMALE

    @JsonProperty("DateOfBirth")
    private Timestamp dateOfBirth;  // 出生年月日

    @JsonProperty("Nationality")
    private String nationality;  // 國籍

    @JsonProperty("PassportNumber")
    private String passportNumber; // 護照號碼/身份證字號

    @JsonProperty("Department")
    private String department;  // 科別

    @JsonProperty("Affiliation")
    private String affiliation;  // 所屬機構

    @JsonProperty("CityOfAffiliation")
    private String cityOfAffiliation;  // 所屬城市

    @JsonProperty("CountryOfAffiliation")
    private String countryOfAffiliation;  // 所屬國家

    @JsonProperty("TelNumber")
    private String telNumber;  // 電話

    @JsonProperty("MobileNumber")
    private String mobileNumber;  // 手機

    @JsonProperty("Email")
    private String email;  // email

}
