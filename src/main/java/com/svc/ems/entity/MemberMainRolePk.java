package com.svc.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.io.Serial;


@Data
@Embeddable
public class MemberMainRolePk implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = -1091896858500134015L;

    @Size(max = 50)
    @NotNull
    @Column(name = "member_id", nullable = false, length = 50)
    private String memberId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    public MemberMainRolePk() {}

    public MemberMainRolePk(String userId, Long roleId) {
        this.memberId = userId;
        this.roleId = roleId;
    }
}