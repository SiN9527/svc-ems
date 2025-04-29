package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.svc.ems.entity.MemberRoleEventEntity}
 */
@Data
public class GroupRegistrationEventResponse implements Serializable {

    @JsonProperty("RegistrationMainDtoList")
    private List<RegistrationMainDto> registrationMainDtoList;

    @JsonProperty("RegistrationPaymentInfoDto")
    private RegistrationPaymentInfoDto registrationPaymentInfoDto;


}