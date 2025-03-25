package com.svc.ems.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class AdminMainRolePkEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 50)
    private long userId;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private long roleId;

    public AdminMainRolePkEntity() {}

    public AdminMainRolePkEntity(long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }



}
