package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link com.svc.ems.entity.RegistrationExtraEntity}
 */
@Data
public class RegistrationExtraDto implements Serializable {

    @JsonProperty("RegistrationId")
    String registrationId;

    @JsonProperty("AttendGalaDinner")
    String attendGalaDinner;


    @JsonProperty("AttendTour")
    String attendTour;

    @JsonProperty("AttendWelcomeReception")
    String attendWelcomeReception;

    @JsonProperty("IsVegetarian")
    String isVegetarian;
    @JsonProperty("ItemName")
    String itemName;
    @JsonProperty("Status")
    String status;

    @JsonProperty("Fee")
    Integer fee;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
}