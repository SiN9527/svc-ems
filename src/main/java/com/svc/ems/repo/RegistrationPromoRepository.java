package com.svc.ems.repo;

import com.svc.ems.entity.RegistrationPromoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationPromoRepository extends JpaRepository<RegistrationPromoEntity, Long> {
}