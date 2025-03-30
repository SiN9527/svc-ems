package com.svc.ems.repo;

import com.svc.ems.entity.AdminMainRoleEntity;
import com.svc.ems.entity.AdminMainRolePkEntity;
import com.svc.ems.entity.AdminRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AdminMainRoleRepository extends JpaRepository<AdminMainRoleEntity, AdminMainRolePkEntity> {

    @Query("SELECT r FROM AdminRoleEntity r " +
            "JOIN AdminMainRoleEntity ur ON r.id = ur.pk.roleId " + // 直接使用 ur.roleId
            "WHERE ur.pk.adminId = :adminId") // 直接使用 ur.adminId
    List<AdminRoleEntity> findRolesByAdminId(@Param("adminId") long userId);


}
