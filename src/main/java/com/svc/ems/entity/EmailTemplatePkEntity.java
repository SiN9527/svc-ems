package com.svc.ems.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class EmailTemplatePkEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer emailId;


    private String eventId;


}
