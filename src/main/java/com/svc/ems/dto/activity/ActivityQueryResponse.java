package com.svc.ems.dto.activity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ActivityQueryResponse {

    /** 活動ID */
    @JsonProperty("EventId")
    private String eventId;

    /** 活動名稱 */
    @JsonProperty("EventName")
    private String eventName;
}
