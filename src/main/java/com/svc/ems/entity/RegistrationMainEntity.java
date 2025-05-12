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
    @Column(name = "registration_id", nullable = false, length = 36, insertable = false, updatable = false)
    private String registrationId;

    @Size(max = 36)
    @NotNull
    @Column(name = "event_id")
    private String eventId;

    @Size(max = 36)
    @NotNull
    @Column(name = "member_id")
    private String memberId;

    @Size(max = 20)
    @Column(name = "group_code")
    private String groupCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "registration_type")
    private String registrationType;


    @NotNull
    @Column(name = "any_accompanying_person")
    private Boolean anyAccompanyingPerson = false;

    @NotNull
    @Column(name = "is_domestic")
    private Boolean isDomestic = false;

    @NotNull
    @Column(name = "fee_amount")
    private Integer feeAmount;

    @NotNull
    @Lob
    @Column(name = "payment_status")
    private String paymentStatus;

    @NotNull
    @Lob
    @Column(name = "registration_status")
    private String registrationStatus;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @NotNull
    @Column(name = "is_group_main")
    private Boolean isGroupMain = false;

//    =============================================
    @OneToOne(mappedBy = "registrationMain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id")
    private RegistrationDetailEntity registrationDetail;

    @OneToOne(mappedBy = "registrationMain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id")
    private RegistrationExtraEntity registrationExtra;

    @OneToOne(mappedBy = "registrationMain", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", referencedColumnName = "registration_id")
    private RegistrationPaymentInfoEntity registrationPaymentInfo;
}