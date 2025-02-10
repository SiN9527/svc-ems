package com.svc.ems.repo;

import com.svc.ems.entity.UserMainRole;
import com.svc.ems.entity.UserMainRolePk;
import com.svc.ems.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserMainRoleRepository extends JpaRepository<UserMainRole, UserMainRolePk> {

    @Query("SELECT r FROM UserRole r " +
            "JOIN UserMainRole ur ON r.id = ur.pk.roleId " + // 直接使用 ur.roleId
            "WHERE ur.pk.userId = :userId") // 直接使用 ur.userId
    List<UserRole> findRolesByUserId(@Param("userId") long userId);


}
