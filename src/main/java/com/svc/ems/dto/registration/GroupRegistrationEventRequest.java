package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.svc.ems.entity.MemberRoleEventEntity}
 */
@Data
public class GroupRegistrationEventRequest implements Serializable {

    @JsonProperty("RegistrationMainDto")
    private List<RegistrationMainDto> registrationMainDto;

    @JsonProperty("RegistrationDetailDto")
    private List<RegistrationMainDto> registrationDetailDto;

    @JsonProperty("RegistrationExtraDto")
    private List <RegistrationExtraDto> registrationExtraDto;




}