package com.svc.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class AccompanyingPersonEntityId implements Serializable {
    private static final long serialVersionUID = -2099145621875497104L;

    private Integer id;


    private Integer memberFollowedId;



}