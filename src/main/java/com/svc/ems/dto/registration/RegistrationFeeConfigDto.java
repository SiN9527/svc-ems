package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * DTO for {@link com.svc.ems.entity.RegistrationFeeConfigEntity}
 */
@Value
public class RegistrationFeeConfigDto implements Serializable {



    @NotNull
    @JsonProperty("EventId")
    @Size(max = 50)
    String eventId;
    @NotNull
    @JsonProperty("Category")
    @Size(max = 100)
    String category;
    @NotNull
    @JsonProperty("Region")
    String region;
    @NotNull
    @JsonProperty("Phase")
    String phase;
    @NotNull
    @JsonProperty("ExchangeRateUsTw")
    BigDecimal exchangeRateUsTw;
    @NotNull
    @JsonProperty("AmountNtd")
    BigDecimal amountNtd;
    @JsonProperty("AmountUsd")
    BigDecimal amountUsd;
    @JsonProperty("IsActive")
    Boolean isActive;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
    @JsonProperty("UpdatedAt")
    Timestamp updatedAt;
}