package com.svc.ems.repo;

import com.svc.ems.entity.MemberMainRoleEntity;
import com.svc.ems.entity.MemberMainRolePkEntity;
import com.svc.ems.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberMainRoleRepository extends JpaRepository<MemberMainRoleEntity, MemberMainRolePkEntity> {

    @Query("SELECT m FROM MemberRoleEntity m " +
            "JOIN MemberMainRoleEntity mr ON m.id = mr.pk.roleId " + // 直接使用 ur.roleId
            "WHERE mr.pk.memberId = :memberId") // 直接使用 ur.userId
    List<MemberRoleEntity> findRolesByMemberId(@Param("memberId") String memberId);
}
