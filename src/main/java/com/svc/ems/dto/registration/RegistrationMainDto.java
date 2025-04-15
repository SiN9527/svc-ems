package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svc.ems.entity.RegistrationMainEntity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * DTO for {@link RegistrationMainEntity}
 */
@Data
public class RegistrationMainDto implements Serializable {
    @Size(max = 36)
    @JsonProperty("RegistrationId")
    String registrationId;
    @NotNull
    @JsonProperty("EventId")
    @Size(max = 36)
    String eventId;

    @JsonProperty("MemberId")
    @NotNull
    @Size(max = 36)
    String memberId;
    @JsonProperty("GroupCode")
    @Size(max = 20)
    String groupCode;
    @NotNull
    @JsonProperty("RegistrationType")
    @Size(max = 50)
    String registrationType;
    @NotNull
    @JsonProperty("IsDomestic")
    Boolean isDomestic;
    @NotNull
    @JsonProperty("FeeAmount")
    Integer feeAmount;
    @JsonProperty("PaymentStatus")
    @NotNull
    String paymentStatus;
    @JsonProperty("RegistrationStatus")
    @NotNull
    String registrationStatus;
    @JsonProperty("CreatedBy")
    Timestamp createdAt;
    @JsonProperty("UpdatedAt")
    Timestamp updatedAt;

    @NotNull
    @JsonProperty("IsGroupMain")
    Boolean isGroupMain;

}