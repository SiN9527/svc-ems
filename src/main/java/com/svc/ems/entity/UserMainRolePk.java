package com.svc.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class UserMainRolePk implements Serializable {

    private static final long serialVersionUID = -1091896858500134015L;

    @Size(max = 50)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    public UserMainRolePk() {}

    public UserMainRolePk(String userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }



}
