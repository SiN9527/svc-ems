package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "MEMBER_MAIN", schema = "db_001")
public class MemberMain {

    @Id
    @Size(max = 50)
    @Column(name = "member_id", nullable = false, length = 50)
    private String memberId;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 20)
    @Column(name = "title", length = 20)
    private String title;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Lob
    @Column(name = "gender")
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 100)
    @Column(name = "department", length = 100)
    private String department;

    @Size(max = 100)
    @Column(name = "affiliation_organization", length = 100)
    private String affiliationOrganization;

    @Size(max = 50)
    @Column(name = "city_of_affiliation", length = 50)
    private String cityOfAffiliation;

    @Size(max = 50)
    @Column(name = "country_of_affiliation", length = 50)
    private String countryOfAffiliation;

    @Size(max = 30)
    @Column(name = "tel_number", length = 30)
    private String telNumber;

    @Size(max = 30)
    @Column(name = "mobile", length = 30)
    private String mobile;

    @Column(name = "registration_date")
    private Timestamp registrationDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Size(max = 50)
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}