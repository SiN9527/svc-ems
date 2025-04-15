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
@Table(name = "REGISTRATION_GROUP_MAIN", schema = "ems_001")
public class RegistrationGroupMainEntity {
    @Id
    @Size(max = 20)
    @Column(name = "group_id", nullable = false, length = 20)
    private String groupId;

    @Size(max = 50)
    @NotNull
    @Column(name = "event_id", nullable = false, length = 50)
    private String eventId;

    @Size(max = 100)
    @Column(name = "group_name", length = 100)
    private String groupName;

    @Size(max = 100)
    @NotNull
    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;

    @Size(max = 100)
    @NotNull
    @Column(name = "contact_email", nullable = false, length = 100)
    private String contactEmail;

    @Size(max = 50)
    @Column(name = "contact_phone", length = 50)
    private String contactPhone;

    @Column(name = "group_size")
    private Integer groupSize;

    @Lob
    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}