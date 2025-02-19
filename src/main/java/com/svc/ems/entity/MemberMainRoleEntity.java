package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
@Data
@Entity
@Table(name = "MEMBER_MAIN_ROLE", schema = "db_001")
public class MemberMainRoleEntity {

    @EmbeddedId
    private MemberMainRolePkEntity pk; // 複合主鍵

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Size(max = 50)
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    /**
     * 設定 `created_at` 和 `updated_at` 預設值
     */
    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }



}