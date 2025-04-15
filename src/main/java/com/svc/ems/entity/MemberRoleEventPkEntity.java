package com.svc.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class MemberRoleEventPkEntity implements Serializable {
    private static final long serialVersionUID = 6760098313863708792L;

    private String roleCode;


    private String memberId;


    private String eventId;



}