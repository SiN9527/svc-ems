package com.svc.ems.repo;

import com.svc.ems.entity.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoleRepository  extends JpaRepository<MemberRoleEntity, Integer> {

    
}
