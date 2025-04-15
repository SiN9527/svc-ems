package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.svc.ems.entity.RegistrationDetailEntity}
 */
@Value
public class RegistrationDetailDto implements Serializable {
    @NotNull
    @Size(max = 20)
    @JsonProperty("EventId")
    String title;
    @NotNull
    @Size(max = 50)
    @JsonProperty("EventId")
    String firstName;
    @Size(max = 50)
    @JsonProperty("EventId")
    String middleName;
    @NotNull
    @Size(max = 50)
    @JsonProperty("EventId")
    String lastName;
    @Size(max = 50)
    @JsonProperty("EventId")
    String fullNameEn;
    @NotNull
    @JsonProperty("EventId")
    String gender;
    @JsonProperty("EventId")
    @NotNull
    Instant dateOfBirth;
    @JsonProperty("EventId")
    @NotNull
    @Size(max = 50)
    String nationality;
    @JsonProperty("EventId")
    @Size(max = 50)
    String passportNumber;
    @JsonProperty("EventId")
    @Size(max = 100)
    String department;
    @JsonProperty("EventId")
    @Size(max = 100)
    String affiliation;
    @JsonProperty("EventId")
    @Size(max = 100)
    String cityOfAffiliation;
    @JsonProperty("EventId")
    @Size(max = 100)
    String countryOfAffiliation;
    @JsonProperty("EventId")
    @Size(max = 50)
    String telNumber;
    @JsonProperty("EventId")
    @Size(max = 50)
    String mobileNumber;
    @JsonProperty("EventId")
    @NotNull
    @Size(max = 100)
    String email;
    @JsonProperty("EventId")
    String dietaryRequest;
    @JsonProperty("EventId")
    Boolean isAccompanyingPerson;
    @Size(max = 50)
    @JsonProperty("EventId")
    String registrationRole;
    @JsonProperty("EventId")
    String uploadUrl;
    @JsonProperty("EventId")
    Instant createdAt;
    @JsonProperty("EventId")
    @Size(max = 50)
    String parentDetailId;
    @JsonProperty("EventId")
    @Size(max = 20)
    String personalId;
}