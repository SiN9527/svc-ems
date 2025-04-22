package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * DTO for {@link com.svc.ems.entity.RegistrationDetailEntity}
 */
@Data
public class RegistrationDetailDto implements Serializable {


    @JsonProperty("Title")
    String title;


    @JsonProperty("FirstName")
    String firstName;


    @Size(max = 50)
    @JsonProperty("LastName")
    String lastName;
    @Size(max = 50)
    @JsonProperty("FullNameCn")
    String fullNameCn;

    @JsonProperty("Gender")
    String gender;
    @JsonProperty("DateOfBirth")

    Timestamp dateOfBirth;
    @JsonProperty("Nationality")

    @Size(max = 50)
    String nationality;
    @JsonProperty("PassportNumber")
    @Size(max = 50)
    String passportNumber;
    @JsonProperty("Department")
    @Size(max = 100)
    String department;
    @JsonProperty("Affiliation")
    @Size(max = 100)
    String affiliation;
    @JsonProperty("CityOfAffiliation")
    @Size(max = 100)
    String cityOfAffiliation;
    @JsonProperty("CountryOfAffiliation")
    @Size(max = 100)
    String countryOfAffiliation;
    @JsonProperty("TelNumber")
    @Size(max = 50)
    String telNumber;
    @JsonProperty("MobileNumber")
    @Size(max = 50)
    String mobileNumber;
    @JsonProperty("Email")

    @Size(max = 100)
    String email;
    @JsonProperty("DietaryRequest")
    String dietaryRequest;
    @JsonProperty("IsAccompanyingPerson")
    Boolean isAccompanyingPerson;
    @Size(max = 50)
    @JsonProperty("RegistrationRole")
    String registrationRole;
    @JsonProperty("UploadUrl")
    String uploadUrl;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
    @JsonProperty("ParentDetailId")
    @Size(max = 50)
    String parentDetailId;

    @JsonProperty("PersonalId")
    @Size(max = 20)
    String personalId;
}