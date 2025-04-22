package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
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


    @Column(name = "registration_id", nullable = false)
    private  String  registrationId;


    @Column(name = "attend_gala_dinner")
    private String attendGalaDinner;


    @Column(name = "attend_tour")
    private String attendTour;



    @Column(name = "attend_welcome_reception")
    private String attendWelcomeReception;

    @Column(name = "is_vegetarian")
    private String isVegetarian;



    @Size(max = 100)
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;


    @Lob
    @Column(name = "status", nullable = false)
    private String status;


    @Column(name = "fee", nullable = false)
    private Integer fee;

    @Column(name = "created_at")
    private Timestamp createdAt;

}