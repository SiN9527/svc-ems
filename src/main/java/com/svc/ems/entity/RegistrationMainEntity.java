package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REGISTRATION_MAIN", schema = "ems_001")
public class RegistrationMainEntity {
    @Id
    @Size(max = 36)
    @Column(name = "registration_id", nullable = false, length = 36)
    private String registrationId;

    @Size(max = 36)
    @NotNull
    @Column(name = "event_id", nullable = false, length = 36)
    private String eventId;

    @Size(max = 36)
    @NotNull
    @Column(name = "member_id", nullable = false, length = 36)
    private String memberId;

    @Size(max = 20)
    @Column(name = "group_code", length = 20)
    private String groupCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "registration_type", nullable = false, length = 50)
    private String registrationType;

    @NotNull
    @Column(name = "is_domestic", nullable = false)
    private Boolean isDomestic = false;

    @NotNull
    @Column(name = "fee_amount", nullable = false)
    private Integer feeAmount;

    @NotNull
    @Lob
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;

    @NotNull
    @Lob
    @Column(name = "registration_status", nullable = false)
    private String registrationStatus;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @NotNull
    @Column(name = "is_group_main", nullable = false)
    private Boolean isGroupMain = false;

}