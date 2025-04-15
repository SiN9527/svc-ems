package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svc.ems.entity.PromotionCodeEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.svc.ems.entity.RegistrationPromoEntity}
 */
@Value
public class RegistrationPromoDto implements Serializable {


    @NotNull
    @JsonProperty("RegistrationMainDto")
    RegistrationMainDto registrationMainEntityDto;
    @NotNull
    @JsonProperty("PromotionCodeDto")
    PromotionCodeEntity promotionCodeDto;

    @JsonProperty("AppliedDiscount")
    Integer appliedDiscount;
}