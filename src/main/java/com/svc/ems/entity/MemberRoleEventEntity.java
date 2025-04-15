package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@IdClass(MemberRoleEventPkEntity.class)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MEMBER_ROLE_EVENT", schema = "ems_001")
public class MemberRoleEventEntity {

    @Id
    @Size(max = 10)
    @NotNull
    @Column(name = "role_code", nullable = false, length = 10)
    private String roleCode;

    @Id
    @Size(max = 50)
    @NotNull
    @Column(name = "member_id", nullable = false, length = 50)
    private String memberId;

    @Id
    @Size(max = 50)
    @NotNull
    @Column(name = "event_id", nullable = false, length = 50)
    private String eventId;

    @Column(name = "created_at")
    private Instant createdAt;

    @Size(max = 50)
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MemberRoleEventEntity that = (MemberRoleEventEntity) o;
        return getRoleCode() != null && Objects.equals(getRoleCode(), that.getRoleCode())
                && getMemberId() != null && Objects.equals(getMemberId(), that.getMemberId())
                && getEventId() != null && Objects.equals(getEventId(), that.getEventId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(roleCode, memberId, eventId);
    }
}