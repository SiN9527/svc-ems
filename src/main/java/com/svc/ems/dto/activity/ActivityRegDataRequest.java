package com.svc.ems.dto.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActivityRegDataRequest {

    /** 活動ID */
    @JsonProperty("EventId")
    private String eventId;

    /** 個人或團體 */
    @JsonProperty("RegType")
    private String regType;

    /** 與會者信箱 */
    @JsonProperty("Email")
    private String email;

    /** 名字 */
    @JsonProperty("FirstName")
    private String firstName;

    /** 姓氏 */
    @JsonProperty("LastName")
    private String lastName;

    /** 國家 */
    @JsonProperty("CountryOfAffiliation")
    private String countryOfAffiliation;
}
