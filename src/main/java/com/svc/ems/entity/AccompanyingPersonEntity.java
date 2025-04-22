package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Table(name = "ACCOMPANYING_PEOPLE", schema = "ems_001")
@Data
@Builder
@IdClass(AccompanyingPersonPkEntity.class)
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "AccompanyingPersonEntity")

public class AccompanyingPersonEntity {

    @NotNull
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Id
    @Column(name = "member_followed_id", nullable = false)
    private String memberFollowedId;

    @Size(max = 50)
    @Column(name = "full_name", length = 50)
    private String fullName;

    @Size(max = 50)
    @Column(name = "nationality", length = 50)
    private String nationality;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "is_vegetarian")
    private Boolean isVegetarian;

    @Size(max = 50)
    @Column(name = "passport_or_id", length = 50)
    private String passportOrId;

    @Size(max = 50)
    @Column(name = "upload_no", length = 50)
    private String uploadNo;

    @Column(name = "create_at")
    private Timestamp createAt;

}