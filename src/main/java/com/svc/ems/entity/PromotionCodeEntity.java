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
@Table(name = "PROMOTION_CODE", schema = "ems_001")
public class PromotionCodeEntity {
    @Id
    @Size(max = 20)
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Lob
    @Column(name = "discount_type", nullable = false)
    private String discountType;

    @NotNull
    @Column(name = "discount_value", nullable = false)
    private Integer discountValue;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count")
    private Integer usedCount;

    @Column(name = "valid_until")
    private Instant validUntil;

    @Column(name = "created_at")
    private Instant createdAt;

}