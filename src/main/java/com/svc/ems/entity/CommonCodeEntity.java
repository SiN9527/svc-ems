package com.svc.ems.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.Instant;

@Data
@Entity
@IdClass(CommonCodePkEntity.class)
@Table(name = "COMMON_CODE", schema = "db_001")
public class CommonCodeEntity {

    @Id
    @Size(max = 255)
    @NotNull
    @Column(name = "code_type", nullable = false)
    private String codeType;

    @Id
    @Size(max = 255)
    @NotNull
    @Column(name = "code_option", nullable = false)
    private String codeOption;

    @Id
    @NotNull
    @Column(name = "ind_codeseq", nullable = false)
    private Integer indCodeseq;
    @Size(max = 255)
    @Column(name = "parent_option")
    private String parentOption;

    @Size(max = 255)
    @NotNull
    @Column(name = "code_name", nullable = false)
    private String codeName;

    @Size(max = 255)
    @Column(name = "code_tag")
    private String codeTag;

    @Size(max = 255)
    @Column(name = "lang_key")
    private String langKey;

    @Size(max = 255)
    @Column(name = "created_user")
    private String createdUser;

    @Column(name = "created_time")
    private Instant createdTime;

    @Size(max = 255)
    @Column(name = "modified_user")
    private String modifiedUser;

    @Column(name = "modified_time")
    private Instant modifiedTime;

}