package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "MEMBER_EVENT", schema = "ems_001")
public class MemberEventEntity {
    @Id
    @Column(name = "member_id", nullable = false)
    private String memberId;

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