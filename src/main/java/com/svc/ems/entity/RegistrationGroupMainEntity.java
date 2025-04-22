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
@Data
@Entity
@Table(name = "REGISTRATION_GROUP_MAIN", schema = "ems_001")
public class RegistrationGroupMainEntity {
    @Id
    @Size(max = 20)
    @Column(name = "group_id", nullable = false, length = 20)
    private String groupId;

    @Size(max = 50)

    @Column(name = "event_id", nullable = false, length = 50)
    private String eventId;

    @Size(max = 100)
    @Column(name = "group_name", length = 100)
    private String groupName;

    @Size(max = 100)
    @Column(name = "contact_name", nullable = false, length = 100)
    private String contactName;

    @Size(max = 100)

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
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}