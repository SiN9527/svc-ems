package com.svc.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
@Data
public class MemberEventPkEntity implements java.io.Serializable {
    private static final long serialVersionUID = -7664510360659768710L;

    private String memberId;


    private String eventId;



}