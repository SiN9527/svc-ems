package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "USER_ROLE", schema = "db_001")
public class UserRoleEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 10)
    @NotNull
    @Column(name = "role_code", nullable = false, length = 10)
    private String roleCode;

    @Size(max = 50)
    @NotNull
    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    @Size(max = 255)
    @Column(name = "role_desc")
    private String roleDesc;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;


    @Column(name = "created_at")
    private Timestamp createdAt;

    @Size(max = 50)
    @Column(name = "updated_by", length = 50)
    private String updatedBy;


    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}