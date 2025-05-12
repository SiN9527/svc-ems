package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "REGISTRATION_EXTRA", schema = "ems_001")
public class RegistrationExtraEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "extra_id", nullable = false)
    private Long id;


    @Column(name = "registration_id", nullable = false, insertable = false, updatable = false)
    private  String  registrationId;


    @Column(name = "attend_gala_dinner")
    private boolean attendGalaDinner;


    @Column(name = "attend_tour")
    private boolean attendTour;

    @Column(name = "attend_welcome_reception")
    private boolean attendWelcomeReception;

    @Column(name = "is_vegetarian")
    private boolean isVegetarian;

    @Column(name = "fee", nullable = false)
    private Integer fee;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @OneToOne
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id")
    private RegistrationMainEntity registrationMain;
}