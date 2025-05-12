package com.svc.ems.dto.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ActivityRegDataResponse {

    /** 註冊ID */
    @JsonProperty("RegistrationId")
    private String registrationId;

    /** 信箱 */
    @JsonProperty("Email")
    private String email;

    /** 英文姓名 */
    @JsonProperty("FullName")
    private String fullName;

    /** 中文姓名 */
    @JsonProperty("CnName")
    private  String cnName;

    /** 報名狀態 */
    @JsonProperty("RegistrationStatus")
    private String registrationStatus;

    /** 付款狀態 */
    @JsonProperty("PaymentStatus")
    private String paymentStatus;

    /** 費用 */
    @JsonProperty("Fee")
    private String fee;

    /** 報名日期 */
    @JsonProperty("ReqDate")
    private String reqDate;
}
