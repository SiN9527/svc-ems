package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@Builder
@Table(name = "REGISTRATION_DETAIL", schema = "ems_001")
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id", nullable = false)
    private Long id;


    @Column(name = "registration_id", nullable = false, insertable = false, updatable = false)
    private  String  registrationId;

    @Size(max = 20)
    @NotNull
    @Column(name = "title", nullable = false, length = 20)
    private String title;

    @Size(max = 50)
    @NotNull
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Size(max = 50)
    @NotNull
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Size(max = 50)
    @Column(name = "full_name_cn", length = 50)
    private String fullNameCn;

    @NotNull
    @Lob
    @Column(name = "gender", nullable = false)
    private String gender;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private Timestamp dateOfBirth;

    @Size(max = 50)
    @NotNull
    @Column(name = "nationality", nullable = false, length = 50)
    private String nationality;

    @Size(max = 50)
    @Column(name = "passport_number", length = 50)
    private String passportNumber;

    @Size(max = 100)
    @Column(name = "department", length = 100)
    private String department;

    @Size(max = 100)
    @Column(name = "affiliation", length = 100)
    private String affiliation;

    @Size(max = 100)
    @Column(name = "city_of_affiliation", length = 100)
    private String cityOfAffiliation;

    @Size(max = 100)
    @Column(name = "country_of_affiliation", length = 100)
    private String countryOfAffiliation;

    @Size(max = 50)
    @Column(name = "tel_number", length = 50)
    private String telNumber;

    @Size(max = 50)
    @Column(name = "mobile_number", length = 50)
    private String mobileNumber;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;


    @Column(name = "is_accompanying_person")
    private Boolean isAccompanyingPerson;

    @Size(max = 50)
    @Column(name = "registration_role", length = 50)
    private String registrationRole;

    @Lob
    @Column(name = "upload_url")
    private String uploadUrl;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Size(max = 50)
    @Column(name = "parent_detail_id", length = 50)
    private String parentDetailId;

    @Size(max = 20)
    @Column(name = "personal_id", length = 20)
    private String personalId;

    @OneToOne
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id")
    private RegistrationMainEntity registrationMain;
}