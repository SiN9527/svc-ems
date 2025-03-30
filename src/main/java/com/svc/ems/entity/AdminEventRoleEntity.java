package com.svc.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "ADMIN_EVENT_ROLE", schema = "ems_001")
@Data
@Builder
@IdClass(AdminEventRolePkEntity.class)
@AllArgsConstructor
@NoArgsConstructor

public class AdminEventRoleEntity {

    @Id
    @Column(name = "admin_id", nullable = false)
    private long adminId;

    @Id
    @Column(name = "event_id", nullable = false)
    private String eventId;
    @Id
    @Column(name = "role_id", nullable = false)
    private long roleId;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;


}
