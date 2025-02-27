package com.svc.ems.dto.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;


@Data
public class EmailTemplateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("EmailId")
    private Integer emailId;

    @JsonProperty("EventId")
    private String eventId;

    @JsonProperty("TemplateType")
    private String templateType; // REGISTER_SUCCESS, PASSWORD_RESET...

    @JsonProperty("Subject")
    private String subject;

    @JsonProperty("Content")
    private String content; // 這裡可以存帶占位符的 HTML 內容

    @JsonProperty("CreateBy")
    private String createBy;

    @JsonProperty("UpdateBy")
    private String updateBy;

    @JsonProperty("CreatedAt")
    private Timestamp createdAt;

    @JsonProperty("UpdatedAt")
    private Timestamp updatedAt;

    @JsonProperty("EventStartDate")
    private Timestamp eventStartDate;

    @JsonProperty("EventEndDate")
    private Timestamp eventEndDate;
}
