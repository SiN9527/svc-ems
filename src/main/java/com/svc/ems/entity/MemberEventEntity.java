package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "MEMBER_EVENT", schema = "ems_001")
@Data
@Builder
@IdClass(MemberEventPkEntity.class)
@AllArgsConstructor
@NoArgsConstructor
public class MemberEventEntity {

    @Size(max = 50)
    @NotNull
    @Column(name = "member_id", nullable = false, length = 50)
    @Id
    private String memberId;

    @Size(max = 50)
    @NotNull
    @Column(name = "event_id", nullable = false, length = 50)
    @Id
    private String eventId;

    @Column(name = "created_at")
    private Timestamp createdAt;

}