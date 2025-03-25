package com.svc.ems.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class AdminEventRolePkEntity implements Serializable {
    private static final long serialVersionUID = 1L;



    private long emailId;


    private String eventId;

    private long roleId;


}
