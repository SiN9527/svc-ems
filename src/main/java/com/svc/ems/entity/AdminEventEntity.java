package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "ADMIN_EVENT", schema = "ems_001")
public class AdminEventEntity {
    @Id
    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(name = "event_id")
    private String eventId;

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