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
@Table(name = "AUTH_TOKEN", schema = "db_001")
public class AuthToken {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "member_id")
    private Long memberId;

    @Size(max = 1024)
    @NotNull
    @Column(name = "token", nullable = false, length = 1024)
    private String token;

    @NotNull
    @Column(name = "expired_at", nullable = false)
    private Timestamp expiredAt;

    @NotNull
    @Column(name = "created_at")
    private Timestamp createdAt;


    /**
     * 設定 `created_at` 和 `updated_at` 預設值
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = new Timestamp(System.currentTimeMillis());

    }


}