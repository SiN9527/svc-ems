package com.svc.ems.repo;

import com.svc.ems.entity.AccompanyingPersonEntity;
import com.svc.ems.entity.AdminEventPkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccompanyingPersonEntityRepository extends JpaRepository<AccompanyingPersonEntity, AdminEventPkEntity> {
}