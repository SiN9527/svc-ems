package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * DTO for {@link com.svc.ems.entity.PromotionCodeEntity}
 */
@Value
public class PromotionCodeDto implements Serializable {
    @Size(max = 20)
    @JsonProperty("Code")
    String code;
    @Size(max = 255)
    @JsonProperty("Description")
    String description;
    @NotNull
    @JsonProperty("DiscountType")
    String discountType;
    @NotNull
    @JsonProperty("DiscountValue")
    Integer discountValue;
    @JsonProperty("UsageLimit")
    Integer usageLimit;
    @JsonProperty("UsedCount")
    Integer usedCount;
    @JsonProperty("ValidUntil")
    Timestamp validUntil;
    @JsonProperty("CreatedAt")
    Timestamp createdAt;
}