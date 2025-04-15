package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "REGISTRATION_FEE_CONFIG", schema = "ems_001")
public class RegistrationFeeConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @NotNull
    @Column(name = "event_id", nullable = false, length = 50)
    private String eventId;

    @Size(max = 100)
    @NotNull
    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @NotNull
    @Lob
    @Column(name = "region", nullable = false)
    private String region;

    @NotNull
    @Lob
    @Column(name = "phase", nullable = false)
    private String phase;

    @NotNull
    @Column(name = "exchange_rate_us_tw", nullable = false, precision = 10, scale = 2)
    private BigDecimal exchangeRateUsTw;

    @NotNull
    @Column(name = "amount_ntd", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountNtd;

    @Column(name = "amount_usd", precision = 10, scale = 2)
    private BigDecimal amountUsd;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}