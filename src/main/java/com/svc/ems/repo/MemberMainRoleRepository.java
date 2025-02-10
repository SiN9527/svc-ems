package com.svc.ems.repo;

import com.svc.ems.entity.MemberMainRole;
import com.svc.ems.entity.MemberMainRolePk;
import com.svc.ems.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberMainRoleRepository extends JpaRepository<MemberMainRole, MemberMainRolePk> {

    @Query("SELECT m FROM MemberRole m " +
            "JOIN MemberMainRole mr ON m.id = mr.pk.roleId " + // 直接使用 ur.roleId
            "WHERE mr.pk.memberId = :memberId") // 直接使用 ur.userId
    List<MemberRole> findRolesByMemberId(@Param("memberId") String memberId);
}
