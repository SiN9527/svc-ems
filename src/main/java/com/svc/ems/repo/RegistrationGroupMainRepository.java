package com.svc.ems.repo;

import com.svc.ems.entity.RegistrationGroupMainEntity;
import com.svc.ems.entity.RegistrationMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationGroupMainRepository extends JpaRepository<RegistrationGroupMainEntity, String> {
    List<RegistrationGroupMainEntity> findByEventIdOrderByGroupIdAsc(String eventId);

    List<RegistrationMainEntity> findByEventIdAndGroupId(String eventId, Integer groupId);
}