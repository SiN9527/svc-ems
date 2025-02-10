package com.svc.ems.repo;

import com.svc.ems.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoleRepository  extends JpaRepository<MemberRole, Integer> {

    
}
