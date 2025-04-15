package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * DTO for {@link com.svc.ems.entity.RegistrationExtraEntity}
 */
@Value
public class RegistrationExtraDto implements Serializable {

    @NotNull
    @Size(max = 100)
    @JsonProperty("ItemName")
    String itemName;
    @NotNull
    @JsonProperty("Status")
    String status;
    @NotNull
    @JsonProperty("Fee")
    Integer fee;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
}