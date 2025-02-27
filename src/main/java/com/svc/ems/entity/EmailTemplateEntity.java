package com.svc.ems.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "EMAIL_TEMPLATE", schema = "db_001")
@Data
@Builder
@IdClass(EmailTemplatePkEntity.class)
@AllArgsConstructor
@NoArgsConstructor

public class EmailTemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email_id", nullable = false)
    private Integer emailId;

    @Id
    @Column(name = "event_id", nullable = false)
    private String eventId;

    @Column(name = "template_type", nullable = false, length = 50)
    private String templateType; // REGISTER_SUCCESS, PASSWORD_RESET...

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content; // 這裡可以存帶占位符的 HTML 內容

    @Column(name = "create_by", nullable = false)
    private String createBy;

    @Column(name = "update_by", nullable = false)
    private String updateBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "event_start_date")
    private Timestamp eventStartDate;

    @Column(name = "event_end_date")
    private Timestamp eventEndDate;
}
