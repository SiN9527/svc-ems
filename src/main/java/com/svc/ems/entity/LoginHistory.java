package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@Table(name = "LOGIN_HISTORY", schema = "db_001")
public class LoginHistory {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "member_id")
    private Long memberId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "login_time")
    private Timestamp loginTime;

    @Size(max = 45)
    @NotNull
    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @NotNull
    @Lob
    @Column(name = "user_agent", nullable = false)
    private String userAgent;

}