package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svc.ems.entity.RegistrationMainEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * DTO for {@link RegistrationMainEntity}
 */
@Data
public class GroupRegistrationMainDto implements Serializable {

    @JsonProperty("RegistrationId")
    String registrationId;

    @JsonProperty("EventId")
    String eventId;

    @JsonProperty("MemberId")
    String memberId;
    @JsonProperty("GroupCode")

    String groupCode;

    @JsonProperty("RegistrationType")

    String registrationType;

    @JsonProperty(" AnyAccompanyingPerson")
    Boolean anyAccompanyingPerson;

    @JsonProperty("IsDomestic")
    Boolean isDomestic;

    @JsonProperty("FeeAmount")
    Integer feeAmount;
    @JsonProperty("PaymentStatus")
    String paymentStatus;

    @JsonProperty("RegistrationStatus")
    String registrationStatus;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
    @JsonProperty("UpdatedAt")
    Timestamp updatedAt;

    @JsonProperty("IsGroupMain")
    Boolean isGroupMain;

    @JsonProperty("RegistrationDetailDto")
    private RegistrationDetailDto registrationDetailDto;

    @JsonProperty("RegistrationExtraDto")
    private RegistrationExtraDto registrationExtraDto;

    @JsonProperty("AccompanyingPersonDtoList")
    private List<AccompanyingPersonDto> accompanyingPersonDtoList;

}