package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "REGISTRATION_EXTRA", schema = "ems_001")
public class RegistrationExtraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "extra_id", nullable = false)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    @NotNull
    @Lob
    @Column(name = "status", nullable = false)
    private String status;

    @NotNull
    @Column(name = "fee", nullable = false)
    private Integer fee;

    @Column(name = "created_at")
    private Instant createdAt;

}