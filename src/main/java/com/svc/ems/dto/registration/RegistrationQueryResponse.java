package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.svc.ems.entity.MemberRoleEventEntity}
 */
@Data
@Builder
public class RegistrationQueryResponse implements Serializable {

    @JsonProperty("MemberMainDto")
    private MemberMainDto memberMainDto;


}