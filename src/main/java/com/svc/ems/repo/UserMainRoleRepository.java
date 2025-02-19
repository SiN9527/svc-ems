package com.svc.ems.repo;

import com.svc.ems.entity.UserMainRoleEntity;
import com.svc.ems.entity.UserMainRolePkEntity;
import com.svc.ems.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserMainRoleRepository extends JpaRepository<UserMainRoleEntity, UserMainRolePkEntity> {

    @Query("SELECT r FROM UserRoleEntity r " +
            "JOIN UserMainRoleEntity ur ON r.id = ur.pk.roleId " + // 直接使用 ur.roleId
            "WHERE ur.pk.userId = :userId") // 直接使用 ur.userId
    List<UserRoleEntity> findRolesByUserId(@Param("userId") long userId);


}
