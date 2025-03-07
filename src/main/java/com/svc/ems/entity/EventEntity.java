package com.svc.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "EVENT", schema = "ems_001")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventEntity {

    @Id
    @Column(name = "event_id", length = 36, nullable = false)
    private String eventId; // UUID 格式的活動 ID

    @Column(name = "event_name", length = 255, nullable = false)
    private String eventName; // 活動名稱

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 活動描述

    @Column(name = "organizer", length = 255, nullable = false)
    private String organizer; // 主辦方

    @Column(name = "location", length = 255)
    private String location; // 活動地點

    @Column(name = "event_start_date", nullable = false)
    private Timestamp eventStartDate; // 活動開始時間

    @Column(name = "event_end_date", nullable = false)
    private Timestamp eventEndDate; // 活動結束時間

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private Timestamp createdAt = Timestamp.from(Instant.now()); // 建立時間 (不可更新)

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private Timestamp updatedAt = Timestamp.from(Instant.now()); // 更新時間 (可更新)


}