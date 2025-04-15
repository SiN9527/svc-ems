package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "REGISTRATION_PROMO", schema = "ems_001")
public class RegistrationPromoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "registration_id", nullable = false)
    private RegistrationMainEntity registration;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "code", nullable = false)
    private PromotionCodeEntity code;

    @NotNull
    @Column(name = "applied_discount", nullable = false)
    private Integer appliedDiscount;

}