package com.svc.ems.repo;

import com.svc.ems.entity.AdminRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRoleEntity, Long> {

    
}
