package com.svc.ems.repo;

import com.svc.ems.entity.RegistrationDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationDetailRepository extends JpaRepository<RegistrationDetailEntity, Long> {
}