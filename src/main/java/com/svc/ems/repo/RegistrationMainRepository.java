package com.svc.ems.repo;

import com.svc.ems.entity.RegistrationMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegistrationMainRepository extends JpaRepository<RegistrationMainEntity, String> {
    Optional<RegistrationMainEntity> findByEventIdAndMemberId(String eventId, String memberId);
}