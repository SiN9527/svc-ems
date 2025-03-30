package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
@Table(name = "ADMIN_MAIN", schema = "ems_001")
public class AdminMainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 設定自動生成 ID
    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Size(max = 100)
    @NotNull
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "event_id", length = 50, nullable = false)
    private String eventId; // UUID 格式的活動 ID

    @Size(max = 50)
    @NotNull
    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Size(max = 50)
    @Column(name = "user_dept", length = 50)
    private String userDept;

    @NotNull
    @ColumnDefault("0")
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