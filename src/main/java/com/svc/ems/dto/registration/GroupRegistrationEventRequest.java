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

    @JsonProperty("GroupRegistrationMainDto")
    private List<GroupRegistrationMainDto> groupRegistrationMainDto;








}