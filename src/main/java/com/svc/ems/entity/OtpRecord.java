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
@Table(name = "OTP_RECORDS", schema = "ems_001")
public class OtpRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Size(max = 20)
    @NotNull
    @Column(name = "otp_code", nullable = false, length = 20)
    private String otpCode;

    @NotNull
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

}